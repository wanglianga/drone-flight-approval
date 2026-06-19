package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.TakeoffReportRequest;
import com.drone.approval.entity.TakeoffReport;
import com.drone.approval.service.TakeoffReportService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/takeoff-reports")
public class TakeoffReportController {

    private TakeoffReportService takeoffReportService;

    public TakeoffReportController(TakeoffReportService takeoffReportService) {
        this.takeoffReportService = takeoffReportService;
    }

    @GetMapping("/mission/{missionId}")
    public ApiResponse<List<TakeoffReport>> getReportsByMission(@PathVariable Long missionId) {
        return ApiResponse.success(takeoffReportService.getReportsByMission(missionId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TakeoffReport> getReportById(@PathVariable Long id) {
        return ApiResponse.success(takeoffReportService.getReportById(id));
    }

    @PostMapping
    public ApiResponse<TakeoffReport> createReport(@Valid @RequestBody TakeoffReportRequest request) {
        return ApiResponse.success("起飞报备成功", takeoffReportService.createReport(request));
    }
}
