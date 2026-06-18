package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NoFlyZoneRequest {

    @NotBlank(message = "禁飞区名称不能为空")
    private String name;

    @NotBlank(message = "禁飞区类型不能为空")
    private String type;

    @NotNull(message = "中心纬度不能为空")
    private BigDecimal centerLatitude;

    @NotNull(message = "中心经度不能为空")
    private BigDecimal centerLongitude;

    @NotNull(message = "半径不能为空")
    @Positive(message = "半径必须为正数")
    private Double radiusMeters;

    @NotNull(message = "最低高度不能为空")
    private Double minAltitudeMeters;

    @NotNull(message = "最高高度不能为空")
    @Positive(message = "最高高度必须为正数")
    private Double maxAltitudeMeters;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;

    private String reason;
}
