package com.drone.approval.service;

import com.drone.approval.dto.TakeoffReportRequest;
import com.drone.approval.entity.Drone;
import com.drone.approval.entity.Mission;
import com.drone.approval.entity.Pilot;
import com.drone.approval.entity.TakeoffReport;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.DroneRepository;
import com.drone.approval.repository.MissionRepository;
import com.drone.approval.repository.PilotRepository;
import com.drone.approval.repository.TakeoffReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TakeoffReportService {

    private TakeoffReportRepository takeoffReportRepository;
    private MissionRepository missionRepository;
    private PilotRepository pilotRepository;
    private DroneRepository droneRepository;

    public TakeoffReportService(TakeoffReportRepository takeoffReportRepository, MissionRepository missionRepository, PilotRepository pilotRepository, DroneRepository droneRepository) {
        this.takeoffReportRepository = takeoffReportRepository;
        this.missionRepository = missionRepository;
        this.pilotRepository = pilotRepository;
        this.droneRepository = droneRepository;
    }

    @Transactional(readOnly = true)
    public List<TakeoffReport> getReportsByMission(Long missionId) {
        return takeoffReportRepository.findByMissionId(missionId);
    }

    @Transactional(readOnly = true)
    public TakeoffReport getReportById(Long id) {
        return takeoffReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TakeoffReport", id));
    }

    @Transactional(readOnly = true)
    public TakeoffReport getActiveReportByMission(Long missionId) {
        return takeoffReportRepository.findActiveReportByMissionId(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("No active takeoff report for mission: " + missionId));
    }

    @Transactional
    public TakeoffReport createReport(TakeoffReportRequest request) {
        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", request.getMissionId()));

        if (mission.getStatus() != Mission.MissionStatus.APPROVED) {
            throw new BusinessException("只有已审批通过的任务才能进行起飞报备，当前状态: " + mission.getStatus());
        }

        if (takeoffReportRepository.hasActiveReport(request.getMissionId())) {
            TakeoffReport duplicate = new TakeoffReport();
            duplicate.setMission(mission);
            duplicate.setPilotName(mission.getPilot().getName());
            duplicate.setTakeoffLatitude(request.getTakeoffLatitude());
            duplicate.setTakeoffLongitude(request.getTakeoffLongitude());
            duplicate.setActualTakeoffTime(request.getActualTakeoffTime());
            duplicate.setBatteryLevelPercent(request.getBatteryLevelPercent());
            duplicate.setWeatherAtTakeoff(request.getWeatherAtTakeoff());
            duplicate.setNumberOfBatteries(request.getNumberOfBatteries());
            duplicate.setPreflightCheckNotes(request.getPreflightCheckNotes());
            duplicate.setStatus(TakeoffReport.TakeoffStatus.DUPLICATE);
            takeoffReportRepository.save(duplicate);

            throw new BusinessException("该任务已存在有效的起飞报备，请勿重复报备");
        }

        if (!isWithinReportWindow(mission, request.getActualTakeoffTime())) {
            throw new BusinessException("起飞报备时间不在允许的报备窗口内。"
                    + "报备窗口：" + mission.getReportWindowStart()
                    + " 至 " + mission.getReportWindowEnd()
                    + "。如已超时，请联系审批人重新确认。");
        }

        LocalDateTime startTime = request.getActualTakeoffTime().minusMinutes(5);
        LocalDateTime endTime = request.getActualTakeoffTime().plusMinutes(5);
        Optional<TakeoffReport> existingReport = takeoffReportRepository.findDuplicateReport(
                request.getMissionId(), startTime, endTime);

        if (existingReport.isPresent()) {
            TakeoffReport duplicate = new TakeoffReport();
            duplicate.setMission(mission);
            duplicate.setPilotName(mission.getPilot().getName());
            duplicate.setTakeoffLatitude(request.getTakeoffLatitude());
            duplicate.setTakeoffLongitude(request.getTakeoffLongitude());
            duplicate.setActualTakeoffTime(request.getActualTakeoffTime());
            duplicate.setBatteryLevelPercent(request.getBatteryLevelPercent());
            duplicate.setWeatherAtTakeoff(request.getWeatherAtTakeoff());
            duplicate.setNumberOfBatteries(request.getNumberOfBatteries());
            duplicate.setPreflightCheckNotes(request.getPreflightCheckNotes());
            duplicate.setStatus(TakeoffReport.TakeoffStatus.DUPLICATE);
            takeoffReportRepository.save(duplicate);

            throw new BusinessException("检测到重复报备：已存在相近时间的起飞报备，报告ID: "
                    + existingReport.get().getId());
        }

        Pilot pilot = mission.getPilot();
        Drone drone = mission.getDrone();

        mission.setStatus(Mission.MissionStatus.IN_PROGRESS);
        missionRepository.save(mission);

        drone.setStatus(Drone.DroneStatus.IN_USE);
        droneRepository.save(drone);

        TakeoffReport report = new TakeoffReport();
        report.setMission(mission);
        report.setPilotName(pilot.getName());
        report.setTakeoffLatitude(request.getTakeoffLatitude());
        report.setTakeoffLongitude(request.getTakeoffLongitude());
        report.setActualTakeoffTime(request.getActualTakeoffTime());
        report.setBatteryLevelPercent(request.getBatteryLevelPercent());
        report.setWeatherAtTakeoff(request.getWeatherAtTakeoff());
        report.setNumberOfBatteries(request.getNumberOfBatteries());
        report.setPreflightCheckNotes(request.getPreflightCheckNotes());
        report.setStatus(TakeoffReport.TakeoffStatus.REPORTED);

        return takeoffReportRepository.save(report);
    }

    @Transactional
    public TakeoffReport cancelReport(Long reportId, String reason) {
        TakeoffReport report = takeoffReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("TakeoffReport", reportId));

        if (report.getStatus() != TakeoffReport.TakeoffStatus.REPORTED) {
            throw new BusinessException("只有已报备的记录可以取消，当前状态: " + report.getStatus());
        }

        report.setStatus(TakeoffReport.TakeoffStatus.CANCELLED);
        report.setCancelReason(reason);
        takeoffReportRepository.save(report);

        Mission mission = report.getMission();
        mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        missionRepository.save(mission);

        Drone drone = mission.getDrone();
        if (drone != null && drone.getStatus() == Drone.DroneStatus.IN_USE) {
            drone.setStatus(Drone.DroneStatus.AVAILABLE);
            droneRepository.save(drone);
        }

        return report;
    }

    @Transactional
    public Mission cancelTakeoffDueToWeather(Long missionId, String reason) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", missionId));

        if (mission.getStatus() != Mission.MissionStatus.APPROVED
                && mission.getStatus() != Mission.MissionStatus.IN_PROGRESS) {
            throw new BusinessException("当前任务状态不支持天气取消，状态: " + mission.getStatus());
        }

        Optional<TakeoffReport> activeReport = takeoffReportRepository.findActiveReportByMissionId(missionId);
        if (activeReport.isPresent()) {
            TakeoffReport report = activeReport.get();
            report.setStatus(TakeoffReport.TakeoffStatus.CANCELLED);
            report.setCancelReason("天气原因: " + reason);
            takeoffReportRepository.save(report);
        }

        mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        mission.setWeatherCondition("天气取消: " + reason);
        missionRepository.save(mission);

        Drone drone = mission.getDrone();
        if (drone != null && drone.getStatus() == Drone.DroneStatus.IN_USE) {
            drone.setStatus(Drone.DroneStatus.AVAILABLE);
            droneRepository.save(drone);
        }

        return mission;
    }

    @Transactional
    public Mission handleTimeoutMissedTakeoff(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", missionId));

        if (mission.getStatus() != Mission.MissionStatus.APPROVED) {
            throw new BusinessException("只有已审批状态的任务需要处理超时未飞，当前状态: " + mission.getStatus());
        }

        if (mission.getReportWindowEnd() == null
                || mission.getReportWindowEnd().isAfter(LocalDateTime.now())) {
            throw new BusinessException("报备窗口尚未结束，无需处理超时");
        }

        mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        missionRepository.save(mission);

        return mission;
    }

    @Transactional
    public Mission changeDrone(Long missionId, Long newDroneId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", missionId));

        if (mission.getStatus() == Mission.MissionStatus.IN_PROGRESS
                || mission.getStatus() == Mission.MissionStatus.COMPLETED) {
            throw new BusinessException("飞行中或已完成的任务不能更换无人机，当前状态: " + mission.getStatus());
        }

        Drone newDrone = droneRepository.findById(newDroneId)
                .orElseThrow(() -> new ResourceNotFoundException("Drone", newDroneId));

        if (newDrone.getStatus() != Drone.DroneStatus.AVAILABLE) {
            throw new BusinessException("新选择的无人机状态不可用，状态: " + newDrone.getStatus());
        }

        Drone oldDrone = mission.getDrone();
        if (oldDrone != null && oldDrone.getId().equals(newDroneId)) {
            throw new BusinessException("新无人机与原无人机相同，无需更换");
        }

        mission.setDrone(newDrone);
        mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        missionRepository.save(mission);

        return mission;
    }

    @Transactional
    public Mission changeTakeoffPoint(Long missionId, BigDecimal newLatitude, BigDecimal newLongitude) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", missionId));

        if (mission.getStatus() == Mission.MissionStatus.IN_PROGRESS
                || mission.getStatus() == Mission.MissionStatus.COMPLETED) {
            throw new BusinessException("飞行中或已完成的任务不能改起降点，当前状态: " + mission.getStatus());
        }

        mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        missionRepository.save(mission);

        return mission;
    }

    private boolean isWithinReportWindow(Mission mission, LocalDateTime takeoffTime) {
        if (mission.getReportWindowStart() == null || mission.getReportWindowEnd() == null) {
            return true;
        }
        return !takeoffTime.isBefore(mission.getReportWindowStart())
                && !takeoffTime.isAfter(mission.getReportWindowEnd());
    }
}
