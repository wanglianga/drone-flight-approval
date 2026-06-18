package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "track_points")
public class TrackPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_log_id", nullable = false)
    private FlightLog flightLog;

    @Column(nullable = false)
    private Integer sequenceNumber;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Double altitudeMeters;

    private Double speedMps;

    private Integer satelliteCount;

    private Double batteryPercent;

    private Boolean isAbnormal;
}
