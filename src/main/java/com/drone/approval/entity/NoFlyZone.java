package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "no_fly_zones")
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
        PERMANENT, TEMPORARY, AIRPORT, MILITARY, GOVERNMENT, EVENT
    }

    public enum NoFlyZoneStatus {
        ACTIVE, EXPIRED, CANCELLED
    }
}
