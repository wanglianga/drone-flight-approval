package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PilotRequest {

    @NotBlank(message = "飞手执照编号不能为空")
    private String licenseNumber;

    @NotBlank(message = "飞手姓名不能为空")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    private String phone;

    private String email;

    @NotNull(message = "执照签发日期不能为空")
    private LocalDate licenseIssueDate;

    @NotNull(message = "执照有效期不能为空")
    private LocalDate licenseExpiryDate;

    @NotNull(message = "飞手等级不能为空")
    private String level;

    private String qualificationFileUrl;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(LocalDate licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getQualificationFileUrl() {
        return qualificationFileUrl;
    }

    public void setQualificationFileUrl(String qualificationFileUrl) {
        this.qualificationFileUrl = qualificationFileUrl;
    }
}
