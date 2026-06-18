package com.drone.approval.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TakeoffReportRequest {

    @NotNull(message = "任务ID不能为空")
    private Long missionId;

    @NotNull(message = "实际起飞时间不能为空")
    private LocalDateTime actualTakeoffTime;

    @NotNull(message = "起飞纬度不能为空")
    private BigDecimal takeoffLatitude;

    @NotNull(message = "起飞经度不能为空")
    private BigDecimal takeoffLongitude;

    @NotNull(message = "电池电量不能为空")
    private Double batteryLevelPercent;

    private String weatherAtTakeoff;

    @NotNull(message = "电池数量不能为空")
    private Integer numberOfBatteries;

    private String preflightCheckNotes;
}
