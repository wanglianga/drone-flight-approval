package com.drone.approval.service;

import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.dto.FlightLogRequest;
import com.drone.approval.entity.*;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightLogService {

    private final FlightLogRepository flightLogRepository;
    private final MissionRepository missionRepository;
    private final DroneRepository droneRepository;
    private final ConflictDetectionService conflictDetectionService;

    @Transactional(readOnly = true)
    public List<FlightLog> getLogsByMission(Long missionId) {
        return flightLogRepository.findByMissionId(missionId);
    }

    @Transactional(readOnly = true)
    public FlightLog getLogById(Long id) {
        return flightLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlightLog", id));
    }

    @Transactional(readOnly = true)
    public FlightLog getLatestLog(Long missionId) {
        return flightLogRepository.findTopByMissionIdOrderByCreatedAtDesc(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("No flight log found for mission: " + missionId));
    }

    @Transactional(readOnly = true)
    public ConflictCheckResult checkLogConflicts(Long logId) {
        FlightLog log = getLogById(logId);
        return conflictDetectionService.checkFlightLogConflicts(log);
    }

    @Transactional
    public FlightLog createFlightLog(FlightLogRequest request) {
        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", request.getMissionId()));

        if (mission.getStatus() != Mission.MissionStatus.IN_PROGRESS
                && mission.getStatus() != Mission.MissionStatus.APPROVED) {
            throw new BusinessException("当前任务状态不允许提交飞行日志，状态: " + mission.getStatus());
        }

        FlightLog.FlightStatus status = FlightLog.FlightStatus.valueOf(request.getFlightStatus());

        if (status == FlightLog.FlightStatus.LOG_MISSING && request.getMissingLogReason() == null) {
            throw new BusinessException("日志缺失状态必须提供缺失原因");
        }

        if (status != FlightLog.FlightStatus.LOG_MISSING
                && (request.getTrackPoints() == null || request.getTrackPoints().isEmpty())) {
            throw new BusinessException("飞行日志必须包含轨迹点，若日志缺失请标记LOG_MISSING状态并说明原因");
        }

        FlightLog log = new FlightLog();
        log.setMission(mission);
        log.setActualStartTime(request.getActualStartTime());
        log.setActualEndTime(request.getActualEndTime());
        log.setActualDurationMinutes(request.getActualDurationMinutes());
        log.setActualMaxAltitudeMeters(request.getActualMaxAltitudeMeters());
        log.setActualDistanceKm(request.getActualDistanceKm());
        log.setBatteryUsedCount(request.getBatteryUsedCount());
        log.setAnomalyDescription(request.getAnomalyDescription());
        log.setFlightStatus(status);
        log.setMissingLogReason(request.getMissingLogReason());

        List<TrackPoint> trackPoints = new ArrayList<>();
        if (request.getTrackPoints() != null) {
            for (FlightLogRequest.TrackPointRequest tpr : request.getTrackPoints()) {
                TrackPoint point = new TrackPoint();
                point.setFlightLog(log);
                point.setSequenceNumber(tpr.getSequenceNumber());
                point.setTimestamp(tpr.getTimestamp());
                point.setLatitude(tpr.getLatitude());
                point.setLongitude(tpr.getLongitude());
                point.setAltitudeMeters(tpr.getAltitudeMeters());
                point.setSpeedMps(tpr.getSpeedMps());
                point.setSatelliteCount(tpr.getSatelliteCount());
                point.setBatteryPercent(tpr.getBatteryPercent());
                point.setIsAbnormal(tpr.getIsAbnormal());
                trackPoints.add(point);
            }
        }
        log.setTrackPoints(trackPoints);

        FlightLog savedLog = flightLogRepository.save(log);

        if (status == FlightLog.FlightStatus.COMPLETED || status == FlightLog.FlightStatus.ABNORMAL
                || status == FlightLog.FlightStatus.INCOMPLETE || status == FlightLog.FlightStatus.LOG_MISSING) {
            mission.setStatus(Mission.MissionStatus.COMPLETED);
            missionRepository.save(mission);

            Drone drone = mission.getDrone();
            drone.setStatus(Drone.DroneStatus.AVAILABLE);
            droneRepository.save(drone);
        }

        return savedLog;
    }

    @Transactional
    public FlightLog updateFlightLog(Long id, FlightLogRequest request) {
        FlightLog log = getLogById(id);

        log.setActualStartTime(request.getActualStartTime());
        log.setActualEndTime(request.getActualEndTime());
        log.setActualDurationMinutes(request.getActualDurationMinutes());
        log.setActualMaxAltitudeMeters(request.getActualMaxAltitudeMeters());
        log.setActualDistanceKm(request.getActualDistanceKm());
        log.setBatteryUsedCount(request.getBatteryUsedCount());
        log.setAnomalyDescription(request.getAnomalyDescription());
        log.setFlightStatus(FlightLog.FlightStatus.valueOf(request.getFlightStatus()));
        log.setMissingLogReason(request.getMissingLogReason());

        return flightLogRepository.save(log);
    }
}
