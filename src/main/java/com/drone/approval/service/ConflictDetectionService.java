package com.drone.approval.service;

import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.entity.*;
import com.drone.approval.repository.*;
import com.drone.approval.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConflictDetectionService {

    private NoFlyZoneRepository noFlyZoneRepository;
    private AirspaceRepository airspaceRepository;
    private MissionRepository missionRepository;
    private InsuranceRepository insuranceRepository;

    public ConflictDetectionService(NoFlyZoneRepository noFlyZoneRepository, AirspaceRepository airspaceRepository, MissionRepository missionRepository, InsuranceRepository insuranceRepository) {
        this.noFlyZoneRepository = noFlyZoneRepository;
        this.airspaceRepository = airspaceRepository;
        this.missionRepository = missionRepository;
        this.insuranceRepository = insuranceRepository;
    }

    public ConflictCheckResult checkMissionConflicts(Mission mission, FlightRoute route) {
        ConflictCheckResult result = new ConflictCheckResult();

        checkAirportClearanceConflicts(route, mission.getPlannedStartTime(), result);
        checkTemporaryRestrictionConflicts(route, mission.getPlannedStartTime(), result);
        checkSchoolZoneConflicts(route, mission.getPlannedStartTime(), result);
        checkEventZoneConflicts(route, mission.getPlannedStartTime(), result);
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

    private void checkAirportClearanceConflicts(FlightRoute route, LocalDateTime checkTime, ConflictCheckResult result) {
        List<NoFlyZone> airportZones = noFlyZoneRepository.findActiveByType(
                NoFlyZone.NoFlyZoneType.AIRPORT, checkTime);

        if (route.getWaypoints() == null || airportZones.isEmpty()) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (NoFlyZone zone : airportZones) {
            List<RoutePoint> conflictPoints = new ArrayList<>();
            for (RoutePoint point : waypoints) {
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
                    conflictPoints.add(point);
                }
            }

            if (!conflictPoints.isEmpty()) {
                RoutePoint first = conflictPoints.get(0);
                RoutePoint last = conflictPoints.get(conflictPoints.size() - 1);

                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("AIRPORT_CLEARANCE");
                conflict.setStartPointSequence(first.getSequenceNumber());
                conflict.setEndPointSequence(last.getSequenceNumber());
                conflict.setStartLatitude(first.getLatitude());
                conflict.setStartLongitude(first.getLongitude());
                conflict.setEndLatitude(last.getLatitude());
                conflict.setEndLongitude(last.getLongitude());
                conflict.setConflictAltitude(calculateAvgAltitude(conflictPoints));
                conflict.setConflictZoneName(zone.getName());
                conflict.setConflictZoneCode(String.valueOf(zone.getId()));
                conflict.setRestrictedMinAltitude(zone.getMinAltitudeMeters());
                conflict.setRestrictedMaxAltitude(zone.getMaxAltitudeMeters());
                conflict.setFlyableStartTime(calculateFlyableStartTime(zone));
                conflict.setFlyableEndTime(calculateFlyableEndTime(zone));
                conflict.setDetourSuggestion("建议绕行：在机场净空区外" + (zone.getRadiusMeters() + 500)
                        + "米处绕行飞行，或申请民航专项审批");
                conflict.setDescription("航段 " + first.getSequenceNumber() + "-" + last.getSequenceNumber()
                        + " 进入机场净空区 '" + zone.getName() + "'"
                        + "，限制高度 " + zone.getMinAltitudeMeters() + "-" + zone.getMaxAltitudeMeters() + "米"
                        + "，原因: 机场净空保护");
                result.addConflict(conflict);
            }
        }
    }

    private void checkTemporaryRestrictionConflicts(FlightRoute route, LocalDateTime checkTime, ConflictCheckResult result) {
        List<NoFlyZone> tempZones = noFlyZoneRepository.findActiveByType(
                NoFlyZone.NoFlyZoneType.TEMPORARY, checkTime);

        if (route.getWaypoints() == null || tempZones.isEmpty()) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (NoFlyZone zone : tempZones) {
            List<RoutePoint> conflictPoints = new ArrayList<>();
            for (RoutePoint point : waypoints) {
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
                    conflictPoints.add(point);
                }
            }

            if (!conflictPoints.isEmpty()) {
                RoutePoint first = conflictPoints.get(0);
                RoutePoint last = conflictPoints.get(conflictPoints.size() - 1);

                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("TEMPORARY_RESTRICTION");
                conflict.setStartPointSequence(first.getSequenceNumber());
                conflict.setEndPointSequence(last.getSequenceNumber());
                conflict.setStartLatitude(first.getLatitude());
                conflict.setStartLongitude(first.getLongitude());
                conflict.setEndLatitude(last.getLatitude());
                conflict.setEndLongitude(last.getLongitude());
                conflict.setConflictAltitude(calculateAvgAltitude(conflictPoints));
                conflict.setConflictZoneName(zone.getName());
                conflict.setConflictZoneCode(String.valueOf(zone.getId()));
                conflict.setRestrictedMinAltitude(zone.getMinAltitudeMeters());
                conflict.setRestrictedMaxAltitude(zone.getMaxAltitudeMeters());
                conflict.setFlyableStartTime(zone.getEffectiveFrom());
                conflict.setFlyableEndTime(zone.getEffectiveTo());
                conflict.setDetourSuggestion("建议绕行：避开管制区范围，或等待管制期结束后飞行。"
                        + "管制有效期：" + zone.getEffectiveFrom() + " 至 " + zone.getEffectiveTo());
                conflict.setDescription("航段 " + first.getSequenceNumber() + "-" + last.getSequenceNumber()
                        + " 进入临时管制区 '" + zone.getName() + "'"
                        + "，限制高度 " + zone.getMinAltitudeMeters() + "-" + zone.getMaxAltitudeMeters() + "米"
                        + "，原因: " + (zone.getReason() != null ? zone.getReason() : "临时管制"));
                result.addConflict(conflict);
            }
        }
    }

    private void checkSchoolZoneConflicts(FlightRoute route, LocalDateTime checkTime, ConflictCheckResult result) {
        List<NoFlyZone> schoolZones = noFlyZoneRepository.findActiveByType(
                NoFlyZone.NoFlyZoneType.SCHOOL, checkTime);

        if (route.getWaypoints() == null || schoolZones.isEmpty()) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (NoFlyZone zone : schoolZones) {
            List<RoutePoint> conflictPoints = new ArrayList<>();
            for (RoutePoint point : waypoints) {
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
                    conflictPoints.add(point);
                }
            }

            if (!conflictPoints.isEmpty()) {
                RoutePoint first = conflictPoints.get(0);
                RoutePoint last = conflictPoints.get(conflictPoints.size() - 1);

                LocalTime schoolStart = LocalTime.of(7, 30);
                LocalTime schoolEnd = LocalTime.of(18, 0);

                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("SCHOOL_ZONE");
                conflict.setStartPointSequence(first.getSequenceNumber());
                conflict.setEndPointSequence(last.getSequenceNumber());
                conflict.setStartLatitude(first.getLatitude());
                conflict.setStartLongitude(first.getLongitude());
                conflict.setEndLatitude(last.getLatitude());
                conflict.setEndLongitude(last.getLongitude());
                conflict.setConflictAltitude(calculateAvgAltitude(conflictPoints));
                conflict.setConflictZoneName(zone.getName());
                conflict.setConflictZoneCode(String.valueOf(zone.getId()));
                conflict.setRestrictedMinAltitude(zone.getMinAltitudeMeters());
                conflict.setRestrictedMaxAltitude(zone.getMaxAltitudeMeters());
                conflict.setFlyableStartTime(checkTime.toLocalDate().atTime(18, 0));
                conflict.setFlyableEndTime(checkTime.toLocalDate().atTime(7, 30).plusDays(1));
                conflict.setDetourSuggestion("建议绕行：学校周边" + (zone.getRadiusMeters() + 200)
                        + "米外绕行，或在非教学时段（18:00-次日7:30）飞行");
                conflict.setDescription("航段 " + first.getSequenceNumber() + "-" + last.getSequenceNumber()
                        + " 进入学校周边管制区 '" + zone.getName() + "'"
                        + "，限制高度 " + zone.getMinAltitudeMeters() + "-" + zone.getMaxAltitudeMeters() + "米"
                        + "，教学时段（7:30-18:00）禁飞");
                result.addConflict(conflict);
            }
        }
    }

    private void checkEventZoneConflicts(FlightRoute route, LocalDateTime checkTime, ConflictCheckResult result) {
        List<NoFlyZone> eventZones = noFlyZoneRepository.findActiveByType(
                NoFlyZone.NoFlyZoneType.EVENT, checkTime);

        if (route.getWaypoints() == null || eventZones.isEmpty()) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (NoFlyZone zone : eventZones) {
            List<RoutePoint> conflictPoints = new ArrayList<>();
            for (RoutePoint point : waypoints) {
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
                    conflictPoints.add(point);
                }
            }

            if (!conflictPoints.isEmpty()) {
                RoutePoint first = conflictPoints.get(0);
                RoutePoint last = conflictPoints.get(conflictPoints.size() - 1);

                ConflictCheckResult.ConflictDetail conflict = new ConflictCheckResult.ConflictDetail();
                conflict.setConflictType("EVENT_ZONE");
                conflict.setStartPointSequence(first.getSequenceNumber());
                conflict.setEndPointSequence(last.getSequenceNumber());
                conflict.setStartLatitude(first.getLatitude());
                conflict.setStartLongitude(first.getLongitude());
                conflict.setEndLatitude(last.getLatitude());
                conflict.setEndLongitude(last.getLongitude());
                conflict.setConflictAltitude(calculateAvgAltitude(conflictPoints));
                conflict.setConflictZoneName(zone.getName());
                conflict.setConflictZoneCode(String.valueOf(zone.getId()));
                conflict.setRestrictedMinAltitude(zone.getMinAltitudeMeters());
                conflict.setRestrictedMaxAltitude(zone.getMaxAltitudeMeters());
                conflict.setFlyableStartTime(zone.getEffectiveFrom());
                conflict.setFlyableEndTime(zone.getEffectiveTo());
                conflict.setDetourSuggestion("建议绕行：大型活动周边" + (zone.getRadiusMeters() + 300)
                        + "米外绕行，活动结束后恢复正常飞行。活动时间："
                        + zone.getEffectiveFrom() + " 至 " + zone.getEffectiveTo());
                conflict.setDescription("航段 " + first.getSequenceNumber() + "-" + last.getSequenceNumber()
                        + " 进入大型活动管制区 '" + zone.getName() + "'"
                        + "，限制高度 " + zone.getMinAltitudeMeters() + "-" + zone.getMaxAltitudeMeters() + "米"
                        + "，原因: " + (zone.getReason() != null ? zone.getReason() : "大型活动管控"));
                result.addConflict(conflict);
            }
        }
    }

    private void checkNoFlyZoneConflicts(FlightRoute route, LocalDateTime checkTime,
                                          ConflictCheckResult result) {
        List<NoFlyZone> activeZones = noFlyZoneRepository.findActiveNoFlyZones(checkTime);

        if (route.getWaypoints() == null) return;

        List<RoutePoint> waypoints = route.getWaypoints();
        for (int i = 0; i < waypoints.size(); i++) {
            RoutePoint point = waypoints.get(i);
            for (NoFlyZone zone : activeZones) {
                if (zone.getType() == NoFlyZone.NoFlyZoneType.AIRPORT
                        || zone.getType() == NoFlyZone.NoFlyZoneType.TEMPORARY
                        || zone.getType() == NoFlyZone.NoFlyZoneType.SCHOOL
                        || zone.getType() == NoFlyZone.NoFlyZoneType.EVENT) {
                    continue;
                }

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
                    conflict.setRestrictedMinAltitude(zone.getMinAltitudeMeters());
                    conflict.setRestrictedMaxAltitude(zone.getMaxAltitudeMeters());
                    conflict.setFlyableStartTime(zone.getEffectiveFrom());
                    conflict.setFlyableEndTime(zone.getEffectiveTo());
                    conflict.setDetourSuggestion("建议绕行：避开该禁飞区，绕行距离约 "
                            + Math.round(GeoUtils.calculateMinDistanceToCircle(
                                    point.getLatitude(), point.getLongitude(),
                                    zone.getCenterLatitude(), zone.getCenterLongitude(),
                                    zone.getRadiusMeters())) + " 米");
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
                    conflict.setRestrictedMinAltitude(airspace.getMinAltitudeMeters());
                    conflict.setRestrictedMaxAltitude(airspace.getMaxAltitudeMeters());
                    conflict.setDetourSuggestion("禁止飞入该空域，请绕开空域边界至少 500 米飞行");
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
                    conflict.setRestrictedMinAltitude(airspace.getMinAltitudeMeters());
                    conflict.setRestrictedMaxAltitude(airspace.getMaxAltitudeMeters());
                    conflict.setDetourSuggestion("需额外审批流程，请提前向空管部门申请进入该空域");
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
                conflict.setRestrictedMaxAltitude(mission.getPlannedMaxAltitude());
                conflict.setDetourSuggestion("建议降低飞行高度至 " + mission.getPlannedMaxAltitude() + " 米以下");
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
            conflict.setFlyableStartTime(m.getPlannedEndTime());
            conflict.setDetourSuggestion("建议更换无人机或调整飞行时间，可在任务 '"
                    + m.getMissionNumber() + "' 结束后（" + m.getPlannedEndTime() + "）安排飞行");
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
                conflict.setDetourSuggestion("涉及跨区域飞行，需向沿途各区域空管部门分别申请审批");
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

    private double calculateAvgAltitude(List<RoutePoint> points) {
        if (points == null || points.isEmpty()) return 0.0;
        double sum = 0.0;
        for (RoutePoint p : points) {
            sum += p.getAltitudeMeters();
        }
        return sum / points.size();
    }

    private LocalDateTime calculateFlyableStartTime(NoFlyZone zone) {
        if (zone.getEffectiveFrom() != null) {
            return zone.getEffectiveFrom();
        }
        return null;
    }

    private LocalDateTime calculateFlyableEndTime(NoFlyZone zone) {
        if (zone.getEffectiveTo() != null) {
            return zone.getEffectiveTo();
        }
        return null;
    }
}
