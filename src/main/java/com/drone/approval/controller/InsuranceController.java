package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.InsuranceRequest;
import com.drone.approval.entity.Insurance;
import com.drone.approval.service.InsuranceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping
    public ApiResponse<List<Insurance>> getAllInsurance() {
        return ApiResponse.success(insuranceService.getAllInsurance());
    }

    @GetMapping("/{id}")
    public ApiResponse<Insurance> getInsuranceById(@PathVariable Long id) {
        return ApiResponse.success(insuranceService.getInsuranceById(id));
    }

    @GetMapping("/drone/{droneId}")
    public ApiResponse<List<Insurance>> getInsuranceByDroneId(@PathVariable Long droneId) {
        return ApiResponse.success(insuranceService.getInsuranceByDroneId(droneId));
    }

    @GetMapping("/drone/{droneId}/valid")
    public ApiResponse<Insurance> getValidInsurance(
            @PathVariable Long droneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkTime) {
        return ApiResponse.success(insuranceService.getValidInsurance(droneId, checkTime));
    }

    @PostMapping
    public ApiResponse<Insurance> createInsurance(@Valid @RequestBody InsuranceRequest request) {
        return ApiResponse.success("保险创建成功", insuranceService.createInsurance(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Insurance> updateInsurance(@PathVariable Long id, @Valid @RequestBody InsuranceRequest request) {
        return ApiResponse.success("保险更新成功", insuranceService.updateInsurance(id, request));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Void> cancelInsurance(@PathVariable Long id) {
        insuranceService.cancelInsurance(id);
        return ApiResponse.success("保险已取消", null);
    }
}
