package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "no_fly_zones")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NoFlyZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoFlyZoneType type;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal centerLatitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal centerLongitude;

    @Column(nullable = false)
    private Double radiusMeters;

    @Column(nullable = false)
    private Double minAltitudeMeters;

    @Column(nullable = false)
    private Double maxAltitudeMeters;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoFlyZoneStatus status = NoFlyZoneStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum NoFlyZoneType {
        PERMANENT, TEMPORARY, AIRPORT, MILITARY, GOVERNMENT, EVENT, SCHOOL
    }

    public enum NoFlyZoneStatus {
        ACTIVE, EXPIRED, CANCELLED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NoFlyZoneType getType() {
        return type;
    }

    public void setType(NoFlyZoneType type) {
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

    public NoFlyZoneStatus getStatus() {
        return status;
    }

    public void setStatus(NoFlyZoneStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
