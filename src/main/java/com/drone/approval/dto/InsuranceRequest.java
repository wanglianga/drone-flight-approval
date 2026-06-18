package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InsuranceRequest {

    @NotNull(message = "无人机ID不能为空")
    private Long droneId;

    @NotBlank(message = "保单号不能为空")
    private String policyNumber;

    @NotBlank(message = "保险公司不能为空")
    private String insuranceCompany;

    @NotNull(message = "保额不能为空")
    @Positive(message = "保额必须为正数")
    private Double coverageAmount;

    @NotNull(message = "生效日期不能为空")
    private LocalDateTime effectiveDate;

    @NotNull(message = "过期日期不能为空")
    private LocalDateTime expiryDate;

    private String policyFileUrl;
}
