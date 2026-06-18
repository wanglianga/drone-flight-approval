package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "airspaces")
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
}
