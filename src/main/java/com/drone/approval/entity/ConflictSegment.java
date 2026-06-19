package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "conflict_segments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConflictSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    @JsonIgnore
    private Approval approval;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConflictType conflictType;

    private Integer startPointSequence;

    private Integer endPointSequence;

    private BigDecimal startLatitude;

    private BigDecimal startLongitude;

    private BigDecimal endLatitude;

    private BigDecimal endLongitude;

    private Double conflictAltitude;

    private String conflictZoneName;

    private String conflictZoneCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public enum ConflictType {
        NO_FLY_ZONE, AIRSPACE_RESTRICTION, HEIGHT_EXCEEDED, TIME_CONFLICT,
        TEMPORARY_RESTRICTION, WEATHER_UNSUITABLE, CROSS_REGION, DRONE_CONFLICT
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval approval) {
        this.approval = approval;
    }

    public ConflictType getConflictType() {
        return conflictType;
    }

    public void setConflictType(ConflictType conflictType) {
        this.conflictType = conflictType;
    }

    public Integer getStartPointSequence() {
        return startPointSequence;
    }

    public void setStartPointSequence(Integer startPointSequence) {
        this.startPointSequence = startPointSequence;
    }

    public Integer getEndPointSequence() {
        return endPointSequence;
    }

    public void setEndPointSequence(Integer endPointSequence) {
        this.endPointSequence = endPointSequence;
    }

    public BigDecimal getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(BigDecimal startLatitude) {
        this.startLatitude = startLatitude;
    }

    public BigDecimal getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(BigDecimal startLongitude) {
        this.startLongitude = startLongitude;
    }

    public BigDecimal getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(BigDecimal endLatitude) {
        this.endLatitude = endLatitude;
    }

    public BigDecimal getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(BigDecimal endLongitude) {
        this.endLongitude = endLongitude;
    }

    public Double getConflictAltitude() {
        return conflictAltitude;
    }

    public void setConflictAltitude(Double conflictAltitude) {
        this.conflictAltitude = conflictAltitude;
    }

    public String getConflictZoneName() {
        return conflictZoneName;
    }

    public void setConflictZoneName(String conflictZoneName) {
        this.conflictZoneName = conflictZoneName;
    }

    public String getConflictZoneCode() {
        return conflictZoneCode;
    }

    public void setConflictZoneCode(String conflictZoneCode) {
        this.conflictZoneCode = conflictZoneCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
