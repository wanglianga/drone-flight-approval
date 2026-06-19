package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

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

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPolicyFileUrl() {
        return policyFileUrl;
    }

    public void setPolicyFileUrl(String policyFileUrl) {
        this.policyFileUrl = policyFileUrl;
    }
}
