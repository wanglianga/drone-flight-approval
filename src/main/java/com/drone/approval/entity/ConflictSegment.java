package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "conflict_segments")
public class ConflictSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
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
}
