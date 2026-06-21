package com.drone.approval.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConflictCheckResult {

    private boolean hasConflict = false;

    private List<ConflictDetail> conflicts = new ArrayList<>();

    public void addConflict(ConflictDetail detail) {
        this.hasConflict = true;
        this.conflicts.add(detail);
    }

    public static class ConflictDetail {

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

        private String description;

        private Double restrictedMinAltitude;

        private Double restrictedMaxAltitude;

        private LocalDateTime flyableStartTime;

        private LocalDateTime flyableEndTime;

        private String detourSuggestion;

        public ConflictDetail() {
        }

        public ConflictDetail(String conflictType, String description) {
            this.conflictType = conflictType;
            this.description = description;
        }

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

    public boolean isHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }

    public List<ConflictDetail> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<ConflictDetail> conflicts) {
        this.conflicts = conflicts;
    }
}
