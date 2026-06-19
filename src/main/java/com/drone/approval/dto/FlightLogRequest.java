package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FlightLogRequest {

    @NotNull(message = "任务ID不能为空")
    private Long missionId;

    @NotNull(message = "实际开始时间不能为空")
    private LocalDateTime actualStartTime;

    @NotNull(message = "实际结束时间不能为空")
    private LocalDateTime actualEndTime;

    @NotNull(message = "实际飞行时长不能为空")
    @Positive(message = "实际飞行时长必须为正数")
    private Integer actualDurationMinutes;

    @NotNull(message = "实际最大高度不能为空")
    @Positive(message = "实际最大高度必须为正数")
    private Double actualMaxAltitudeMeters;

    @NotNull(message = "实际飞行距离不能为空")
    @Positive(message = "实际飞行距离必须为正数")
    private Double actualDistanceKm;

    @NotNull(message = "使用电池数量不能为空")
    @Positive(message = "使用电池数量必须为正数")
    private Integer batteryUsedCount;

    @Valid
    private List<TrackPointRequest> trackPoints;

    private String anomalyDescription;

    @NotNull(message = "飞行状态不能为空")
    private String flightStatus;

    private String missingLogReason;

    public static class TrackPointRequest {

        @NotNull(message = "航迹点序号不能为空")
        private Integer sequenceNumber;

        @NotNull(message = "时间戳不能为空")
        private LocalDateTime timestamp;

        @NotNull(message = "纬度不能为空")
        private BigDecimal latitude;

        @NotNull(message = "经度不能为空")
        private BigDecimal longitude;

        @NotNull(message = "高度不能为空")
        private Double altitudeMeters;

        private Double speedMps;

        private Integer satelliteCount;

        private Double batteryPercent;

        private Boolean isAbnormal;

        public Integer getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
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

        public Integer getSatelliteCount() {
            return satelliteCount;
        }

        public void setSatelliteCount(Integer satelliteCount) {
            this.satelliteCount = satelliteCount;
        }

        public Double getBatteryPercent() {
            return batteryPercent;
        }

        public void setBatteryPercent(Double batteryPercent) {
            this.batteryPercent = batteryPercent;
        }

        public Boolean getIsAbnormal() {
            return isAbnormal;
        }

        public void setIsAbnormal(Boolean isAbnormal) {
            this.isAbnormal = isAbnormal;
        }
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Integer getActualDurationMinutes() {
        return actualDurationMinutes;
    }

    public void setActualDurationMinutes(Integer actualDurationMinutes) {
        this.actualDurationMinutes = actualDurationMinutes;
    }

    public Double getActualMaxAltitudeMeters() {
        return actualMaxAltitudeMeters;
    }

    public void setActualMaxAltitudeMeters(Double actualMaxAltitudeMeters) {
        this.actualMaxAltitudeMeters = actualMaxAltitudeMeters;
    }

    public Double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public void setActualDistanceKm(Double actualDistanceKm) {
        this.actualDistanceKm = actualDistanceKm;
    }

    public Integer getBatteryUsedCount() {
        return batteryUsedCount;
    }

    public void setBatteryUsedCount(Integer batteryUsedCount) {
        this.batteryUsedCount = batteryUsedCount;
    }

    public List<TrackPointRequest> getTrackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(List<TrackPointRequest> trackPoints) {
        this.trackPoints = trackPoints;
    }

    public String getAnomalyDescription() {
        return anomalyDescription;
    }

    public void setAnomalyDescription(String anomalyDescription) {
        this.anomalyDescription = anomalyDescription;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getMissingLogReason() {
        return missingLogReason;
    }

    public void setMissingLogReason(String missingLogReason) {
        this.missingLogReason = missingLogReason;
    }
}
