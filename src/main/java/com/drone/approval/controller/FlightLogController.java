package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.dto.FlightLogRequest;
import com.drone.approval.entity.FlightLog;
import com.drone.approval.service.FlightLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flight-logs")
public class FlightLogController {

    private FlightLogService flightLogService;

    public FlightLogController(FlightLogService flightLogService) {
        this.flightLogService = flightLogService;
    }

    @GetMapping("/mission/{missionId}")
    public ApiResponse<List<FlightLog>> getLogsByMission(@PathVariable Long missionId) {
        return ApiResponse.success(flightLogService.getLogsByMission(missionId));
    }

    @GetMapping("/{id}")
    public ApiResponse<FlightLog> getLogById(@PathVariable Long id) {
        return ApiResponse.success(flightLogService.getLogById(id));
    }

    @GetMapping("/mission/{missionId}/latest")
    public ApiResponse<FlightLog> getLatestLog(@PathVariable Long missionId) {
        return ApiResponse.success(flightLogService.getLatestLog(missionId));
    }

    @GetMapping("/{id}/conflicts")
    public ApiResponse<ConflictCheckResult> checkLogConflicts(@PathVariable Long id) {
        return ApiResponse.success(flightLogService.checkLogConflicts(id));
    }

    @PostMapping
    public ApiResponse<FlightLog> createFlightLog(@Valid @RequestBody FlightLogRequest request) {
        return ApiResponse.success("飞行日志提交成功", flightLogService.createFlightLog(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<FlightLog> updateFlightLog(@PathVariable Long id, @Valid @RequestBody FlightLogRequest request) {
        return ApiResponse.success("飞行日志更新成功", flightLogService.updateFlightLog(id, request));
    }
}
