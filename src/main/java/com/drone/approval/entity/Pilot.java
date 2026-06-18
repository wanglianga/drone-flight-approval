package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pilots")
public class Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String idCard;

    private String phone;

    private String email;

    @Column(nullable = false)
    private LocalDate licenseIssueDate;

    @Column(nullable = false)
    private LocalDate licenseExpiryDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PilotLevel level;

    private String qualificationFileUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PilotStatus status = PilotStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum PilotLevel {
        A, B, C
    }

    public enum PilotStatus {
        ACTIVE, SUSPENDED, EXPIRED
    }
}
