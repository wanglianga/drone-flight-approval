package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.dto.MissionRequest;
import com.drone.approval.entity.Mission;
import com.drone.approval.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public ApiResponse<List<Mission>> getAllMissions() {
        return ApiResponse.success(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    public ApiResponse<Mission> getMissionById(@PathVariable Long id) {
        return ApiResponse.success(missionService.getMissionById(id));
    }

    @GetMapping("/number/{missionNumber}")
    public ApiResponse<Mission> getMissionByNumber(@PathVariable String missionNumber) {
        return ApiResponse.success(missionService.getMissionByNumber(missionNumber));
    }

    @GetMapping("/pilot/{pilotId}")
    public ApiResponse<List<Mission>> getMissionsByPilot(@PathVariable Long pilotId) {
        return ApiResponse.success(missionService.getMissionsByPilot(pilotId));
    }

    @GetMapping("/drone/{droneId}")
    public ApiResponse<List<Mission>> getMissionsByDrone(@PathVariable Long droneId) {
        return ApiResponse.success(missionService.getMissionsByDrone(droneId));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<Mission>> getMissionsByStatus(@PathVariable Mission.MissionStatus status) {
        return ApiResponse.success(missionService.getMissionsByStatus(status));
    }

    @GetMapping("/{id}/conflicts")
    public ApiResponse<ConflictCheckResult> checkMissionConflicts(@PathVariable Long id) {
        return ApiResponse.success(missionService.checkMissionConflicts(id));
    }

    @PostMapping
    public ApiResponse<Mission> createMission(@Valid @RequestBody MissionRequest request) {
        return ApiResponse.success("任务创建成功", missionService.createMission(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Mission> updateMission(@PathVariable Long id, @Valid @RequestBody MissionRequest request) {
        return ApiResponse.success("任务更新成功", missionService.updateMission(id, request));
    }

    @PutMapping("/{id}/submit")
    public ApiResponse<Mission> submitMission(@PathVariable Long id) {
        return ApiResponse.success("任务已提交审批", missionService.submitMission(id));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Mission> cancelMission(@PathVariable Long id) {
        return ApiResponse.success("任务已取消", missionService.cancelMission(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ApiResponse.success("任务已删除", null);
    }
}
