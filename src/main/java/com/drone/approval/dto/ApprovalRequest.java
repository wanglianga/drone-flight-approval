package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ApprovalRequest {

    @NotNull(message = "任务ID不能为空")
    private Long missionId;

    @NotBlank(message = "审批人不能为空")
    private String approver;

    @NotBlank(message = "审批决定不能为空")
    private String decision;

    private String generalComment;

    private LocalDateTime approvedStartTime;

    private LocalDateTime approvedEndTime;

    private Double approvedMaxAltitude;

    @Valid
    private List<ConflictSegmentRequest> conflictSegments;

    public static class ConflictSegmentRequest {

        @NotBlank(message = "冲突类型不能为空")
        private String conflictType;

        private Integer startPointSequence;

        private Integer endPointSequence;

        private BigDecimal startLatitude;

        private BigDecimal startLongitude;

        private BigDecimal endLatitude;

        private BigDecimal endLongitude;

        private Double conflictAltitude;

        private String conflictZoneName;

        private String conflictZoneCode;

        @NotBlank(message = "冲突描述不能为空")
        private String description;

        private Double restrictedMinAltitude;

        private Double restrictedMaxAltitude;

        private LocalDateTime flyableStartTime;

        private LocalDateTime flyableEndTime;

        private String detourSuggestion;

        public String getConflictType() {
            return conflictType;
        }

        public void setConflictType(String conflictType) {
            this.conflictType = conflictType;
        }

        public Integer getStartPointSequence() {
            return startPointSequence;
        }

        public void setStartPointSequence(Integer startPointSequence) {
            this.startPointSequence = startPointSequence;
        }

        public Integer getEndPointSequence() {
            return endPointSequence;
        }

        public void setEndPointSequence(Integer endPointSequence) {
            this.endPointSequence = endPointSequence;
        }

        public BigDecimal getStartLatitude() {
            return startLatitude;
        }

        public void setStartLatitude(BigDecimal startLatitude) {
            this.startLatitude = startLatitude;
        }

        public BigDecimal getStartLongitude() {
            return startLongitude;
        }

        public void setStartLongitude(BigDecimal startLongitude) {
            this.startLongitude = startLongitude;
        }

        public BigDecimal getEndLatitude() {
            return endLatitude;
        }

        public void setEndLatitude(BigDecimal endLatitude) {
            this.endLatitude = endLatitude;
        }

        public BigDecimal getEndLongitude() {
            return endLongitude;
        }

        public void setEndLongitude(BigDecimal endLongitude) {
            this.endLongitude = endLongitude;
        }

        public Double getConflictAltitude() {
            return conflictAltitude;
        }

        public void setConflictAltitude(Double conflictAltitude) {
            this.conflictAltitude = conflictAltitude;
        }

        public String getConflictZoneName() {
            return conflictZoneName;
        }

        public void setConflictZoneName(String conflictZoneName) {
            this.conflictZoneName = conflictZoneName;
        }

        public String getConflictZoneCode() {
            return conflictZoneCode;
        }

        public void setConflictZoneCode(String conflictZoneCode) {
            this.conflictZoneCode = conflictZoneCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getRestrictedMinAltitude() {
            return restrictedMinAltitude;
        }

        public void setRestrictedMinAltitude(Double restrictedMinAltitude) {
            this.restrictedMinAltitude = restrictedMinAltitude;
        }

        public Double getRestrictedMaxAltitude() {
            return restrictedMaxAltitude;
        }

        public void setRestrictedMaxAltitude(Double restrictedMaxAltitude) {
            this.restrictedMaxAltitude = restrictedMaxAltitude;
        }

        public LocalDateTime getFlyableStartTime() {
            return flyableStartTime;
        }

        public void setFlyableStartTime(LocalDateTime flyableStartTime) {
            this.flyableStartTime = flyableStartTime;
        }

        public LocalDateTime getFlyableEndTime() {
            return flyableEndTime;
        }

        public void setFlyableEndTime(LocalDateTime flyableEndTime) {
            this.flyableEndTime = flyableEndTime;
        }

        public String getDetourSuggestion() {
            return detourSuggestion;
        }

        public void setDetourSuggestion(String detourSuggestion) {
            this.detourSuggestion = detourSuggestion;
        }
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
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

    public List<ConflictSegmentRequest> getConflictSegments() {
        return conflictSegments;
    }

    public void setConflictSegments(List<ConflictSegmentRequest> conflictSegments) {
        this.conflictSegments = conflictSegments;
    }
}
