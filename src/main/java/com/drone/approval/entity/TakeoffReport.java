package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "takeoff_reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mission_id", "reportedAt"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TakeoffReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private String pilotName;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal takeoffLatitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal takeoffLongitude;

    @Column(nullable = false)
    private LocalDateTime actualTakeoffTime;

    @Column(nullable = false)
    private Double batteryLevelPercent;

    private String weatherAtTakeoff;

    @Column(nullable = false)
    private Integer numberOfBatteries;

    @Column(columnDefinition = "TEXT")
    private String preflightCheckNotes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TakeoffStatus status = TakeoffStatus.REPORTED;

    private String cancelReason;

    @CreationTimestamp
    private LocalDateTime reportedAt;

    public enum TakeoffStatus {
        REPORTED, DUPLICATE, CANCELLED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public BigDecimal getTakeoffLatitude() {
        return takeoffLatitude;
    }

    public void setTakeoffLatitude(BigDecimal takeoffLatitude) {
        this.takeoffLatitude = takeoffLatitude;
    }

    public BigDecimal getTakeoffLongitude() {
        return takeoffLongitude;
    }

    public void setTakeoffLongitude(BigDecimal takeoffLongitude) {
        this.takeoffLongitude = takeoffLongitude;
    }

    public LocalDateTime getActualTakeoffTime() {
        return actualTakeoffTime;
    }

    public void setActualTakeoffTime(LocalDateTime actualTakeoffTime) {
        this.actualTakeoffTime = actualTakeoffTime;
    }

    public Double getBatteryLevelPercent() {
        return batteryLevelPercent;
    }

    public void setBatteryLevelPercent(Double batteryLevelPercent) {
        this.batteryLevelPercent = batteryLevelPercent;
    }

    public String getWeatherAtTakeoff() {
        return weatherAtTakeoff;
    }

    public void setWeatherAtTakeoff(String weatherAtTakeoff) {
        this.weatherAtTakeoff = weatherAtTakeoff;
    }

    public Integer getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public void setNumberOfBatteries(Integer numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }

    public String getPreflightCheckNotes() {
        return preflightCheckNotes;
    }

    public void setPreflightCheckNotes(String preflightCheckNotes) {
        this.preflightCheckNotes = preflightCheckNotes;
    }

    public TakeoffStatus getStatus() {
        return status;
    }

    public void setStatus(TakeoffStatus status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }
}
