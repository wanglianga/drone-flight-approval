package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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

    @Data
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
    }

    @Data
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
    }
}
