package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "missions")
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
}
