package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "flight_logs")
public class FlightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private LocalDateTime actualStartTime;

    @Column(nullable = false)
    private LocalDateTime actualEndTime;

    @Column(nullable = false)
    private Integer actualDurationMinutes;

    @Column(nullable = false)
    private Double actualMaxAltitudeMeters;

    @Column(nullable = false)
    private Double actualDistanceKm;

    @Column(nullable = false)
    private Integer batteryUsedCount;

    @OneToMany(mappedBy = "flightLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackPoint> trackPoints = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String anomalyDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @Column(columnDefinition = "TEXT")
    private String missingLogReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum FlightStatus {
        COMPLETED, ABNORMAL, INCOMPLETE, LOG_MISSING
    }
}
