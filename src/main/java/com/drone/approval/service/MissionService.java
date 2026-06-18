package com.drone.approval.service;

import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.dto.MissionRequest;
import com.drone.approval.entity.*;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final PilotRepository pilotRepository;
    private final DroneRepository droneRepository;
    private final FlightRouteRepository flightRouteRepository;
    private final ConflictDetectionService conflictDetectionService;

    @Transactional(readOnly = true)
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Mission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", id));
    }

    @Transactional(readOnly = true)
    public Mission getMissionByNumber(String missionNumber) {
        return missionRepository.findByMissionNumber(missionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with number: " + missionNumber));
    }

    @Transactional(readOnly = true)
    public List<Mission> getMissionsByPilot(Long pilotId) {
        return missionRepository.findByPilotId(pilotId);
    }

    @Transactional(readOnly = true)
    public List<Mission> getMissionsByDrone(Long droneId) {
        return missionRepository.findByDroneId(droneId);
    }

    @Transactional(readOnly = true)
    public List<Mission> getMissionsByStatus(Mission.MissionStatus status) {
        return missionRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public ConflictCheckResult checkMissionConflicts(Long missionId) {
        Mission mission = getMissionById(missionId);
        List<FlightRoute> routes = flightRouteRepository.findByMissionId(missionId);
        if (routes.isEmpty()) {
            throw new BusinessException("该任务没有航线信息");
        }
        return conflictDetectionService.checkMissionConflicts(mission, routes.get(0));
    }

    @Transactional
    public Mission createMission(MissionRequest request) {
        Pilot pilot = pilotRepository.findById(request.getPilotId())
                .orElseThrow(() -> new ResourceNotFoundException("Pilot", request.getPilotId()));

        if (pilot.getStatus() != Pilot.PilotStatus.ACTIVE) {
            throw new BusinessException("飞手状态异常，当前状态: " + pilot.getStatus());
        }

        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone", request.getDroneId()));

        if (drone.getStatus() != Drone.DroneStatus.AVAILABLE
                && drone.getStatus() != Drone.DroneStatus.IN_USE) {
            throw new BusinessException("无人机状态异常，当前状态: " + drone.getStatus());
        }

        Mission mission = new Mission();
        mission.setMissionNumber(generateMissionNumber());
        mission.setCompanyName(request.getCompanyName());
        mission.setProjectName(request.getProjectName());
        mission.setType(Mission.MissionType.valueOf(request.getType()));
        mission.setPilot(pilot);
        mission.setDrone(drone);
        mission.setPlannedStartTime(request.getPlannedStartTime());
        mission.setPlannedEndTime(request.getPlannedEndTime());
        mission.setPlannedMaxAltitude(request.getPlannedMaxAltitude());
        mission.setPlannedMaxRadius(request.getPlannedMaxRadius());
        mission.setMissionDescription(request.getMissionDescription());
        mission.setWeatherCondition(request.getWeatherCondition());
        mission.setStatus(Mission.MissionStatus.DRAFT);

        Mission savedMission = missionRepository.save(mission);

        if (request.getRoute() != null) {
            createFlightRoute(savedMission, request.getRoute());
        }

        return savedMission;
    }

    @Transactional
    public Mission updateMission(Long id, MissionRequest request) {
        Mission mission = getMissionById(id);

        if (mission.getStatus() != Mission.MissionStatus.DRAFT
                && mission.getStatus() != Mission.MissionStatus.REJECTED) {
            throw new BusinessException("当前任务状态不允许修改，状态: " + mission.getStatus());
        }

        Pilot pilot = pilotRepository.findById(request.getPilotId())
                .orElseThrow(() -> new ResourceNotFoundException("Pilot", request.getPilotId()));

        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone", request.getDroneId()));

        mission.setCompanyName(request.getCompanyName());
        mission.setProjectName(request.getProjectName());
        mission.setType(Mission.MissionType.valueOf(request.getType()));
        mission.setPilot(pilot);
        mission.setDrone(drone);
        mission.setPlannedStartTime(request.getPlannedStartTime());
        mission.setPlannedEndTime(request.getPlannedEndTime());
        mission.setPlannedMaxAltitude(request.getPlannedMaxAltitude());
        mission.setPlannedMaxRadius(request.getPlannedMaxRadius());
        mission.setMissionDescription(request.getMissionDescription());
        mission.setWeatherCondition(request.getWeatherCondition());

        Mission savedMission = missionRepository.save(mission);

        if (request.getRoute() != null) {
            List<FlightRoute> existingRoutes = flightRouteRepository.findByMissionId(id);
            flightRouteRepository.deleteAll(existingRoutes);
            createFlightRoute(savedMission, request.getRoute());
        }

        return savedMission;
    }

    @Transactional
    public Mission submitMission(Long id) {
        Mission mission = getMissionById(id);

        if (mission.getStatus() != Mission.MissionStatus.DRAFT
                && mission.getStatus() != Mission.MissionStatus.REJECTED) {
            throw new BusinessException("当前任务状态不允许提交，状态: " + mission.getStatus());
        }

        List<FlightRoute> routes = flightRouteRepository.findByMissionId(id);
        if (routes.isEmpty()) {
            throw new BusinessException("任务必须包含航线信息才能提交");
        }

        mission.setStatus(Mission.MissionStatus.SUBMITTED);
        return missionRepository.save(mission);
    }

    @Transactional
    public Mission cancelMission(Long id) {
        Mission mission = getMissionById(id);

        if (mission.getStatus() == Mission.MissionStatus.COMPLETED
                || mission.getStatus() == Mission.MissionStatus.CANCELLED) {
            throw new BusinessException("当前任务状态不允许取消，状态: " + mission.getStatus());
        }

        mission.setStatus(Mission.MissionStatus.CANCELLED);
        return missionRepository.save(mission);
    }

    @Transactional
    public void deleteMission(Long id) {
        Mission mission = getMissionById(id);
        if (mission.getStatus() != Mission.MissionStatus.DRAFT) {
            throw new BusinessException("只有草稿状态的任务可以删除");
        }
        missionRepository.delete(mission);
    }

    private void createFlightRoute(Mission mission, MissionRequest.RouteRequest routeRequest) {
        FlightRoute route = new FlightRoute();
        route.setMission(mission);
        route.setRouteName(routeRequest.getRouteName());
        route.setTotalDistanceKm(routeRequest.getTotalDistanceKm());
        route.setEstimatedDurationMinutes(routeRequest.getEstimatedDurationMinutes());
        route.setMaxAltitudeMeters(routeRequest.getMaxAltitudeMeters());
        route.setCrossRegionInfo(routeRequest.getCrossRegionInfo());

        List<RoutePoint> waypoints = new ArrayList<>();
        if (routeRequest.getWaypoints() != null) {
            for (MissionRequest.WaypointRequest wp : routeRequest.getWaypoints()) {
                RoutePoint point = new RoutePoint();
                point.setRoute(route);
                point.setSequenceNumber(wp.getSequenceNumber());
                point.setLatitude(wp.getLatitude());
                point.setLongitude(wp.getLongitude());
                point.setAltitudeMeters(wp.getAltitudeMeters());
                point.setSpeedMps(wp.getSpeedMps());
                point.setHoldTimeSeconds(wp.getHoldTimeSeconds());
                waypoints.add(point);
            }
        }
        route.setWaypoints(waypoints);

        flightRouteRepository.save(route);
    }

    private String generateMissionNumber() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "MIS" + dateStr + uuid;
    }
}
