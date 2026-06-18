package com.drone.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
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
}
