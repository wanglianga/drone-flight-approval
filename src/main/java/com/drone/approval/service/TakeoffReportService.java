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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeoffReportService {

    private final TakeoffReportRepository takeoffReportRepository;
    private final MissionRepository missionRepository;
    private final PilotRepository pilotRepository;
    private final DroneRepository droneRepository;

    @Transactional(readOnly = true)
    public List<TakeoffReport> getReportsByMission(Long missionId) {
        return takeoffReportRepository.findByMissionId(missionId);
    }

    @Transactional(readOnly = true)
    public TakeoffReport getReportById(Long id) {
        return takeoffReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TakeoffReport", id));
    }

    @Transactional
    public TakeoffReport createReport(TakeoffReportRequest request) {
        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", request.getMissionId()));

        if (mission.getStatus() != Mission.MissionStatus.APPROVED) {
            throw new BusinessException("只有已审批通过的任务才能进行起飞报备，当前状态: " + mission.getStatus());
        }

        Optional<TakeoffReport> existingReport = takeoffReportRepository.findDuplicateReport(
                request.getMissionId(), request.getActualTakeoffTime(), 5);

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
}
