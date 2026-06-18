package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
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
}
