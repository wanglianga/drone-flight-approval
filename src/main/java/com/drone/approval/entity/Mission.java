package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "missions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String missionNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id", nullable = false)
    private Pilot pilot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @Column(nullable = false)
    private LocalDateTime plannedStartTime;

    @Column(nullable = false)
    private LocalDateTime plannedEndTime;

    @Column(nullable = false)
    private Double plannedMaxAltitude;

    @Column(nullable = false)
    private Double plannedMaxRadius;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionStatus status = MissionStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String missionDescription;

    private String weatherCondition;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum MissionType {
        AERIAL_PHOTOGRAPHY, INSPECTION, MAPPING, EMERGENCY
    }

    public enum MissionStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELLED, IN_PROGRESS, COMPLETED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMissionNumber() {
        return missionNumber;
    }

    public void setMissionNumber(String missionNumber) {
        this.missionNumber = missionNumber;
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

    public MissionType getType() {
        return type;
    }

    public void setType(MissionType type) {
        this.type = type;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
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

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
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
