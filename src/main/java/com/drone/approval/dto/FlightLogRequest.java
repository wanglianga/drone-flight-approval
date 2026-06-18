package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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

    @Data
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
    }
}
