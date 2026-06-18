package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "takeoff_reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mission_id", "reportedAt"})
})
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

    @CreationTimestamp
    private LocalDateTime reportedAt;

    public enum TakeoffStatus {
        REPORTED, DUPLICATE, CANCELLED
    }
}
