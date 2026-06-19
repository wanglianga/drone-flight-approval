package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DroneRequest {

    @NotBlank(message = "无人机序列号不能为空")
    private String serialNumber;

    @NotBlank(message = "无人机型号不能为空")
    private String model;

    @NotBlank(message = "制造商不能为空")
    private String manufacturer;

    @NotNull(message = "最大起飞重量不能为空")
    @Positive(message = "最大起飞重量必须为正数")
    private Double maxTakeoffWeight;

    @NotNull(message = "最大飞行高度不能为空")
    @Positive(message = "最大飞行高度必须为正数")
    private Double maxFlightAltitude;

    @NotNull(message = "最大飞行时间不能为空")
    @Positive(message = "最大飞行时间必须为正数")
    private Integer maxFlightTime;

    @NotBlank(message = "电池类型不能为空")
    private String batteryType;

    @NotNull(message = "电池数量不能为空")
    @Positive(message = "电池数量必须为正数")
    private Integer batteryCount;

    private String registrationFileUrl;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getMaxTakeoffWeight() {
        return maxTakeoffWeight;
    }

    public void setMaxTakeoffWeight(Double maxTakeoffWeight) {
        this.maxTakeoffWeight = maxTakeoffWeight;
    }

    public Double getMaxFlightAltitude() {
        return maxFlightAltitude;
    }

    public void setMaxFlightAltitude(Double maxFlightAltitude) {
        this.maxFlightAltitude = maxFlightAltitude;
    }

    public Integer getMaxFlightTime() {
        return maxFlightTime;
    }

    public void setMaxFlightTime(Integer maxFlightTime) {
        this.maxFlightTime = maxFlightTime;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public Integer getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(Integer batteryCount) {
        this.batteryCount = batteryCount;
    }

    public String getRegistrationFileUrl() {
        return registrationFileUrl;
    }

    public void setRegistrationFileUrl(String registrationFileUrl) {
        this.registrationFileUrl = registrationFileUrl;
    }
}
