package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "airspaces")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String region;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AirspaceType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AirspaceRestriction restriction;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum AirspaceType {
        CONTROLLED, UNCONTROLLED, RESTRICTED, PROHIBITED, DANGER
    }

    public enum AirspaceRestriction {
        OPEN, REQUIRE_APPROVAL, PROHIBITED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public AirspaceType getType() {
        return type;
    }

    public void setType(AirspaceType type) {
        this.type = type;
    }

    public AirspaceRestriction getRestriction() {
        return restriction;
    }

    public void setRestriction(AirspaceRestriction restriction) {
        this.restriction = restriction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
