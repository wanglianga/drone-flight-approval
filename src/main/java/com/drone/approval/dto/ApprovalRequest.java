package com.drone.approval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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

    @Data
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
    }
}
