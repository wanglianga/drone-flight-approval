package com.drone.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "approvals")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public ApprovalDecision getDecision() {
        return decision;
    }

    public void setDecision(ApprovalDecision decision) {
        this.decision = decision;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    public List<ConflictSegment> getConflictSegments() {
        return conflictSegments;
    }

    public void setConflictSegments(List<ConflictSegment> conflictSegments) {
        this.conflictSegments = conflictSegments;
    }

    public LocalDateTime getApprovedStartTime() {
        return approvedStartTime;
    }

    public void setApprovedStartTime(LocalDateTime approvedStartTime) {
        this.approvedStartTime = approvedStartTime;
    }

    public LocalDateTime getApprovedEndTime() {
        return approvedEndTime;
    }

    public void setApprovedEndTime(LocalDateTime approvedEndTime) {
        this.approvedEndTime = approvedEndTime;
    }

    public Double getApprovedMaxAltitude() {
        return approvedMaxAltitude;
    }

    public void setApprovedMaxAltitude(Double approvedMaxAltitude) {
        this.approvedMaxAltitude = approvedMaxAltitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
