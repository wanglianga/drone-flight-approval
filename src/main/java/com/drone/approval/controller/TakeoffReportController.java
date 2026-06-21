package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.TakeoffReportRequest;
import com.drone.approval.entity.Mission;
import com.drone.approval.entity.TakeoffReport;
import com.drone.approval.service.TakeoffReportService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/mission/{missionId}/active")
    public ApiResponse<TakeoffReport> getActiveReportByMission(@PathVariable Long missionId) {
        return ApiResponse.success(takeoffReportService.getActiveReportByMission(missionId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TakeoffReport> getReportById(@PathVariable Long id) {
        return ApiResponse.success(takeoffReportService.getReportById(id));
    }

    @PostMapping
    public ApiResponse<TakeoffReport> createReport(@Valid @RequestBody TakeoffReportRequest request) {
        return ApiResponse.success("起飞报备成功", takeoffReportService.createReport(request));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<TakeoffReport> cancelReport(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isEmpty()) {
            reason = "未说明原因";
        }
        return ApiResponse.success("报备已取消", takeoffReportService.cancelReport(id, reason));
    }

    @PostMapping("/mission/{missionId}/weather-cancel")
    public ApiResponse<Mission> cancelTakeoffDueToWeather(@PathVariable Long missionId,
                                                           @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isEmpty()) {
            reason = "天气原因";
        }
        return ApiResponse.success("天气取消，任务已回到待确认状态",
                takeoffReportService.cancelTakeoffDueToWeather(missionId, reason));
    }

    @PostMapping("/mission/{missionId}/handle-timeout")
    public ApiResponse<Mission> handleTimeoutMissedTakeoff(@PathVariable Long missionId) {
        return ApiResponse.success("超时未飞处理完成，任务已回到待确认状态",
                takeoffReportService.handleTimeoutMissedTakeoff(missionId));
    }

    @PutMapping("/mission/{missionId}/change-drone/{droneId}")
    public ApiResponse<Mission> changeDrone(@PathVariable Long missionId,
                                             @PathVariable Long droneId) {
        return ApiResponse.success("更换无人机成功，任务已回到待确认状态",
                takeoffReportService.changeDrone(missionId, droneId));
    }

    @PutMapping("/mission/{missionId}/change-takeoff-point")
    public ApiResponse<Mission> changeTakeoffPoint(@PathVariable Long missionId,
                                                    @RequestBody Map<String, BigDecimal> body) {
        BigDecimal latitude = body.get("latitude");
        BigDecimal longitude = body.get("longitude");
        return ApiResponse.success("更改起降点成功，任务已回到待确认状态",
                takeoffReportService.changeTakeoffPoint(missionId, latitude, longitude));
    }
}
