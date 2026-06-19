package com.drone.approval.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public LocalDateTime getActualTakeoffTime() {
        return actualTakeoffTime;
    }

    public void setActualTakeoffTime(LocalDateTime actualTakeoffTime) {
        this.actualTakeoffTime = actualTakeoffTime;
    }

    public BigDecimal getTakeoffLatitude() {
        return takeoffLatitude;
    }

    public void setTakeoffLatitude(BigDecimal takeoffLatitude) {
        this.takeoffLatitude = takeoffLatitude;
    }

    public BigDecimal getTakeoffLongitude() {
        return takeoffLongitude;
    }

    public void setTakeoffLongitude(BigDecimal takeoffLongitude) {
        this.takeoffLongitude = takeoffLongitude;
    }

    public Double getBatteryLevelPercent() {
        return batteryLevelPercent;
    }

    public void setBatteryLevelPercent(Double batteryLevelPercent) {
        this.batteryLevelPercent = batteryLevelPercent;
    }

    public String getWeatherAtTakeoff() {
        return weatherAtTakeoff;
    }

    public void setWeatherAtTakeoff(String weatherAtTakeoff) {
        this.weatherAtTakeoff = weatherAtTakeoff;
    }

    public Integer getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public void setNumberOfBatteries(Integer numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }

    public String getPreflightCheckNotes() {
        return preflightCheckNotes;
    }

    public void setPreflightCheckNotes(String preflightCheckNotes) {
        this.preflightCheckNotes = preflightCheckNotes;
    }
}
