package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MissionRequest {

    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    @NotBlank(message = "任务类型不能为空")
    private String type;

    @NotNull(message = "飞手ID不能为空")
    private Long pilotId;

    @NotNull(message = "无人机ID不能为空")
    private Long droneId;

    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime plannedStartTime;

    @NotNull(message = "计划结束时间不能为空")
    private LocalDateTime plannedEndTime;

    @NotNull(message = "计划最大高度不能为空")
    @Positive(message = "计划最大高度必须为正数")
    private Double plannedMaxAltitude;

    @NotNull(message = "计划最大半径不能为空")
    @Positive(message = "计划最大半径必须为正数")
    private Double plannedMaxRadius;

    private String missionDescription;

    private String weatherCondition;

    @Valid
    private RouteRequest route;

    public static class RouteRequest {

        @NotBlank(message = "航线名称不能为空")
        private String routeName;

        @NotNull(message = "总距离不能为空")
        @Positive(message = "总距离必须为正数")
        private Double totalDistanceKm;

        @NotNull(message = "预计飞行时长不能为空")
        @Positive(message = "预计飞行时长必须为正数")
        private Integer estimatedDurationMinutes;

        @NotNull(message = "最大高度不能为空")
        @Positive(message = "最大高度必须为正数")
        private Double maxAltitudeMeters;

        @Valid
        private List<WaypointRequest> waypoints;

        private String crossRegionInfo;

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public Double getTotalDistanceKm() {
            return totalDistanceKm;
        }

        public void setTotalDistanceKm(Double totalDistanceKm) {
            this.totalDistanceKm = totalDistanceKm;
        }

        public Integer getEstimatedDurationMinutes() {
            return estimatedDurationMinutes;
        }

        public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
            this.estimatedDurationMinutes = estimatedDurationMinutes;
        }

        public Double getMaxAltitudeMeters() {
            return maxAltitudeMeters;
        }

        public void setMaxAltitudeMeters(Double maxAltitudeMeters) {
            this.maxAltitudeMeters = maxAltitudeMeters;
        }

        public List<WaypointRequest> getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(List<WaypointRequest> waypoints) {
            this.waypoints = waypoints;
        }

        public String getCrossRegionInfo() {
            return crossRegionInfo;
        }

        public void setCrossRegionInfo(String crossRegionInfo) {
            this.crossRegionInfo = crossRegionInfo;
        }
    }

    public static class WaypointRequest {

        @NotNull(message = "航点序号不能为空")
        private Integer sequenceNumber;

        @NotNull(message = "纬度不能为空")
        private BigDecimal latitude;

        @NotNull(message = "经度不能为空")
        private BigDecimal longitude;

        @NotNull(message = "高度不能为空")
        private Double altitudeMeters;

        private Double speedMps;

        private Integer holdTimeSeconds;

        public Integer getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }

        public void setLatitude(BigDecimal latitude) {
            this.latitude = latitude;
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public void setLongitude(BigDecimal longitude) {
            this.longitude = longitude;
        }

        public Double getAltitudeMeters() {
            return altitudeMeters;
        }

        public void setAltitudeMeters(Double altitudeMeters) {
            this.altitudeMeters = altitudeMeters;
        }

        public Double getSpeedMps() {
            return speedMps;
        }

        public void setSpeedMps(Double speedMps) {
            this.speedMps = speedMps;
        }

        public Integer getHoldTimeSeconds() {
            return holdTimeSeconds;
        }

        public void setHoldTimeSeconds(Integer holdTimeSeconds) {
            this.holdTimeSeconds = holdTimeSeconds;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPilotId() {
        return pilotId;
    }

    public void setPilotId(Long pilotId) {
        this.pilotId = pilotId;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalDateTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public Double getPlannedMaxAltitude() {
        return plannedMaxAltitude;
    }

    public void setPlannedMaxAltitude(Double plannedMaxAltitude) {
        this.plannedMaxAltitude = plannedMaxAltitude;
    }

    public Double getPlannedMaxRadius() {
        return plannedMaxRadius;
    }

    public void setPlannedMaxRadius(Double plannedMaxRadius) {
        this.plannedMaxRadius = plannedMaxRadius;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription) {
        this.missionDescription = missionDescription;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public RouteRequest getRoute() {
        return route;
    }

    public void setRoute(RouteRequest route) {
        this.route = route;
    }
}
