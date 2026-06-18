package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private Double maxTakeoffWeight;

    @Column(nullable = false)
    private Double maxFlightAltitude;

    @Column(nullable = false)
    private Integer maxFlightTime;

    @Column(nullable = false)
    private String batteryType;

    @Column(nullable = false)
    private Integer batteryCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DroneStatus status = DroneStatus.AVAILABLE;

    private String registrationFileUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum DroneStatus {
        AVAILABLE, IN_USE, MAINTENANCE, RETIRED
    }
}
