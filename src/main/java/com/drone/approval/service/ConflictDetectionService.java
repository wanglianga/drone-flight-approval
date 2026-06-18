package com.drone.approval.service;

import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.entity.*;
import com.drone.approval.repository.*;
import com.drone.approval.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConflictDetectionService {

    private final NoFlyZoneRepository noFlyZoneRepository;
    private final AirspaceRepository airspaceRepository;
    private final MissionRepository missionRepository;
    private final InsuranceRepository insuranceRepository;

    public ConflictCheckResult checkMissionConflicts(Mission mission, FlightRoute route) {
        ConflictCheckResult result = new ConflictCheckResult();

        checkNoFlyZoneConflicts(route, mission.getPlannedStartTime(), result);
        checkAirspaceConflicts(route, result);
        checkAltitudeConflicts(mission, route, result);
        checkDroneAvailabilityConflicts(mission, result);
        checkInsuranceConflicts(mission, result);
        checkTimeConflicts(mission, result);
        checkCrossRegion(route, result);

        return result;
    }

    public ConflictCheckResult checkFlightLogConflicts(FlightLog log) {
        ConflictCheckResult result = new ConflictCheckResult();

        checkNoFlyZoneConflictsForLog(log, result);
        checkAltitudeConflictsForLog(log, result);

        return result;
    }

    private void checkNoFlyZoneConflicts(FlightRoute route, LocalDateTime checkTime,
                                          ConflictCheckResult result) {
        List<NoFlyZone> activeZones = noFlyZoneRepository.findActiveNoFlyZones(checkTime);

        if (route.getWaypoints() == null) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (int i = 0; i < waypoints.size(); i++) {
            RoutePoint point = waypoints.get(i);
            for (NoFlyZone zone : activeZones) {
                boolean inZone = GeoUtils.isPointInCircle(
                        point.getLatitude(), point.getLongitude(),
                        zone.getCenterLatitude(), zone.getCenterLongitude(),
                        zone.getRadiusMeters()
                );
                boolean altitudeInRange = GeoUtils.isAltitudeInRange(
                        point.getAltitudeMeters(),
                        zone.getMinAltitudeMeters(),
                        zone.getMaxAltitudeMeters()
                );

                if (inZone && altitudeInRange) {
                    ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                    conflict.setConflictType("NO_FLY_ZONE");
                    conflict.setStartPointSequence(point.getSequenceNumber());
                    conflict.setEndPointSequence(point.getSequenceNumber());
                    conflict.setStartLatitude(point.getLatitude());
                    conflict.setStartLongitude(point.getLongitude());
                    conflict.setEndLatitude(point.getLatitude());
                    conflict.setEndLongitude(point.getLongitude());
                    conflict.setConflictAltitude(point.getAltitudeMeters());
                    conflict.setConflictZoneName(zone.getName());
                    conflict.setConflictZoneCode(String.valueOf(zone.getId()));
                    conflict.setDescription("航点 " + point.getSequenceNumber()
                            + " 进入禁飞区 '" + zone.getName() + "'，类型: " + zone.getType()
                            + "，原因: " + (zone.getReason() != null ? zone.getReason() : "未说明"));
                    result.addConflict(conflict);
                }
            }
        }
    }

    private void checkAirspaceConflicts(FlightRoute route, ConflictCheckResult result) {
        List<Airspace> restrictedAirspaces = airspaceRepository.findRestrictedAirspaces();

        if (route.getWaypoints() == null) return;

        for (RoutePoint point : route.getWaypoints()) {
            for (Airspace airspace : restrictedAirspaces) {
                boolean inAirspace = GeoUtils.isPointInCircle(
                        point.getLatitude(), point.getLongitude(),
                        airspace.getCenterLatitude(), airspace.getCenterLongitude(),
                        airspace.getRadiusMeters()
                );
                boolean altitudeInRange = GeoUtils.isAltitudeInRange(
                        point.getAltitudeMeters(),
                        airspace.getMinAltitudeMeters(),
                        airspace.getMaxAltitudeMeters()
                );

                if (inAirspace && altitudeInRange
                        && airspace.getRestriction() == Airspace.AirspaceRestriction.PROHIBITED) {
                    ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                    conflict.setConflictType("AIRSPACE_RESTRICTION");
                    conflict.setStartPointSequence(point.getSequenceNumber());
                    conflict.setEndPointSequence(point.getSequenceNumber());
                    conflict.setStartLatitude(point.getLatitude());
                    conflict.setStartLongitude(point.getLongitude());
                    conflict.setEndLatitude(point.getLatitude());
                    conflict.setEndLongitude(point.getLongitude());
                    conflict.setConflictAltitude(point.getAltitudeMeters());
                    conflict.setConflictZoneName(airspace.getName());
                    conflict.setConflictZoneCode(airspace.getCode());
                    conflict.setDescription("航点 " + point.getSequenceNumber()
                            + " 进入受限空域 '" + airspace.getName() + "' (" + airspace.getCode() + ")"
                            + "，该空域类型为禁止飞行");
                    result.addConflict(conflict);
                } else if (inAirspace && altitudeInRange
                        && airspace.getRestriction() == Airspace.AirspaceRestriction.REQUIRE_APPROVAL) {
                    ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                    conflict.setConflictType("AIRSPACE_RESTRICTION");
                    conflict.setStartPointSequence(point.getSequenceNumber());
                    conflict.setEndPointSequence(point.getSequenceNumber());
                    conflict.setStartLatitude(point.getLatitude());
                    conflict.setStartLongitude(point.getLongitude());
                    conflict.setEndLatitude(point.getLatitude());
                    conflict.setEndLongitude(point.getLongitude());
                    conflict.setConflictAltitude(point.getAltitudeMeters());
                    conflict.setConflictZoneName(airspace.getName());
                    conflict.setConflictZoneCode(airspace.getCode());
                    conflict.setDescription("航点 " + point.getSequenceNumber()
                            + " 进入需审批空域 '" + airspace.getName() + "' (" + airspace.getCode() + ")"
                            + "，需要额外审批流程");
                    result.addConflict(conflict);
                }
            }
        }
    }

    private void checkAltitudeConflicts(Mission mission, FlightRoute route, ConflictCheckResult result) {
        if (route.getWaypoints() == null) return;

        for (RoutePoint point : route.getWaypoints()) {
            if (point.getAltitudeMeters() > mission.getPlannedMaxAltitude()) {
                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("HEIGHT_EXCEEDED");
                conflict.setStartPointSequence(point.getSequenceNumber());
                conflict.setEndPointSequence(point.getSequenceNumber());
                conflict.setStartLatitude(point.getLatitude());
                conflict.setStartLongitude(point.getLongitude());
                conflict.setEndLatitude(point.getLatitude());
                conflict.setEndLongitude(point.getLongitude());
                conflict.setConflictAltitude(point.getAltitudeMeters());
                conflict.setDescription("航点 " + point.getSequenceNumber()
                        + " 高度超限: " + point.getAltitudeMeters() + "米"
                        + "，超过计划最大高度 " + mission.getPlannedMaxAltitude() + "米");
                result.addConflict(conflict);
            }
        }
    }

    private void checkDroneAvailabilityConflicts(Mission mission, ConflictCheckResult result) {
        List<Mission> overlapping = missionRepository.findOverlappingMissionsForDroneExcluding(
                mission.getDrone().getId(),
                mission.getPlannedStartTime(),
                mission.getPlannedEndTime(),
                mission.getId() != null ? mission.getId() : -1L
        );

        for (Mission m : overlapping) {
            ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
            conflict.setConflictType("DRONE_CONFLICT");
            conflict.setConflictZoneName(m.getMissionNumber());
            conflict.setConflictZoneCode(String.valueOf(m.getId()));
            conflict.setDescription("无人机在时间段 " + mission.getPlannedStartTime()
                    + " 至 " + mission.getPlannedEndTime()
                    + " 内已被任务 '" + m.getMissionNumber() + "' (" + m.getProjectName() + ") 使用");
            result.addConflict(conflict);
        }
    }

    private void checkInsuranceConflicts(Mission mission, ConflictCheckResult result) {
        java.util.Optional<Insurance> validInsurance = insuranceRepository.findValidInsuranceByDroneId(
                mission.getDrone().getId(),
                mission.getPlannedStartTime()
        );

        if (validInsurance.isEmpty()) {
            ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
            conflict.setConflictType("TEMPORARY_RESTRICTION");
            conflict.setDescription("无人机在计划飞行时间段内无有效保险，请先购买或续期保险");
            result.addConflict(conflict);
        }
    }

    private void checkTimeConflicts(Mission mission, ConflictCheckResult result) {
        if (mission.getPlannedEndTime().isBefore(mission.getPlannedStartTime())) {
            ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
            conflict.setConflictType("TIME_CONFLICT");
            conflict.setDescription("计划结束时间不能早于开始时间");
            result.addConflict(conflict);
        }

        if (mission.getPlannedStartTime().isBefore(LocalDateTime.now())) {
            ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
            conflict.setConflictType("TIME_CONFLICT");
            conflict.setDescription("计划开始时间不能早于当前时间");
            result.addConflict(conflict);
        }
    }

    private void checkCrossRegion(FlightRoute route, ConflictCheckResult result) {
        if (route.getWaypoints() == null || route.getWaypoints().size() < 2) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            RoutePoint p1 = waypoints.get(i);
            RoutePoint p2 = waypoints.get(i + 1);

            double distance = GeoUtils.calculateDistanceMeters(
                    p1.getLatitude(), p1.getLongitude(),
                    p2.getLatitude(), p2.getLongitude()
            );

            if (distance > 50000) {
                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("CROSS_REGION");
                conflict.setStartPointSequence(p1.getSequenceNumber());
                conflict.setEndPointSequence(p2.getSequenceNumber());
                conflict.setStartLatitude(p1.getLatitude());
                conflict.setStartLongitude(p1.getLongitude());
                conflict.setEndLatitude(p2.getLatitude());
                conflict.setEndLongitude(p2.getLongitude());
                conflict.setDescription("航段 " + p1.getSequenceNumber() + "-" + p2.getSequenceNumber()
                        + " 距离 " + String.format("%.2f", distance / 1000)
                        + "公里，可能涉及跨区域飞行，需要额外审批");
                result.addConflict(conflict);
            }
        }
    }

    private void checkNoFlyZoneConflictsForLog(FlightLog log, ConflictCheckResult result) {
        if (log.getTrackPoints() == null) return;

        List<NoFlyZone> activeZones = noFlyZoneRepository.findActiveNoFlyZones(log.getActualStartTime());

        for (TrackPoint point : log.getTrackPoints()) {
            for (NoFlyZone zone : activeZones) {
                boolean inZone = GeoUtils.isPointInCircle(
                        point.getLatitude(), point.getLongitude(),
                        zone.getCenterLatitude(), zone.getCenterLongitude(),
                        zone.getRadiusMeters()
                );
                boolean altitudeInRange = GeoUtils.isAltitudeInRange(
                        point.getAltitudeMeters(),
                        zone.getMinAltitudeMeters(),
                        zone.getMaxAltitudeMeters()
                );

                if (inZone && altitudeInRange) {
                    ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                    conflict.setConflictType("NO_FLY_ZONE");
                    conflict.setStartPointSequence(point.getSequenceNumber());
                    conflict.setEndPointSequence(point.getSequenceNumber());
                    conflict.setStartLatitude(point.getLatitude());
                    conflict.setStartLongitude(point.getLongitude());
                    conflict.setConflictAltitude(point.getAltitudeMeters());
                    conflict.setConflictZoneName(zone.getName());
                    conflict.setDescription("实际飞行轨迹点 " + point.getSequenceNumber()
                            + " 进入禁飞区 '" + zone.getName() + "'");
                    result.addConflict(conflict);
                }
            }
        }
    }

    private void checkAltitudeConflictsForLog(FlightLog log, ConflictCheckResult result) {
        if (log.getTrackPoints() == null || log.getMission() == null) return;

        Mission mission = log.getMission();
        double maxApprovedAltitude = mission.getPlannedMaxAltitude();

        for (TrackPoint point : log.getTrackPoints()) {
            if (point.getAltitudeMeters() > maxApprovedAltitude) {
                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("HEIGHT_EXCEEDED");
                conflict.setStartPointSequence(point.getSequenceNumber());
                conflict.setEndPointSequence(point.getSequenceNumber());
                conflict.setStartLatitude(point.getLatitude());
                conflict.setStartLongitude(point.getLongitude());
                conflict.setConflictAltitude(point.getAltitudeMeters());
                conflict.setDescription("实际飞行轨迹点 " + point.getSequenceNumber()
                        + " 高度超限: " + point.getAltitudeMeters() + "米"
                        + "，超过审批最大高度 " + maxApprovedAltitude + "米");
                result.addConflict(conflict);
            }
        }
    }
}
