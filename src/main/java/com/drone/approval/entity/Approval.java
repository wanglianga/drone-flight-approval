package com.drone.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "approvals")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private String approver;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalDecision decision;

    @Column(columnDefinition = "TEXT")
    private String generalComment;

    @OneToMany(mappedBy = "approval", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConflictSegment> conflictSegments = new ArrayList<>();

    private LocalDateTime approvedStartTime;

    private LocalDateTime approvedEndTime;

    private Double approvedMaxAltitude;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ApprovalDecision {
        APPROVED, REJECTED, RETURNED_FOR_REVISION
    }
}
