package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(BigDecimal centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public BigDecimal getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(BigDecimal centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public Double getMinAltitudeMeters() {
        return minAltitudeMeters;
    }

    public void setMinAltitudeMeters(Double minAltitudeMeters) {
        this.minAltitudeMeters = minAltitudeMeters;
    }

    public Double getMaxAltitudeMeters() {
        return maxAltitudeMeters;
    }

    public void setMaxAltitudeMeters(Double maxAltitudeMeters) {
        this.maxAltitudeMeters = maxAltitudeMeters;
    }

    public LocalDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDateTime getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
