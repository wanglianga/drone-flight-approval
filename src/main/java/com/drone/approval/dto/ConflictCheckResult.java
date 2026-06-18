package com.drone.approval.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConflictCheckResult {

    private boolean hasConflict = false;

    private List<ConflictDetail> conflicts = new ArrayList<>();

    public void addConflict(ConflictDetail detail) {
        this.hasConflict = true;
        this.conflicts.add(detail);
    }

    @Data
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

        public ConflictDetail() {
        }

        public ConflictDetail(String conflictType, String description) {
            this.conflictType = conflictType;
            this.description = description;
        }
    }
}
