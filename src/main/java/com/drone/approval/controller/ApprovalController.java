package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.ApprovalRequest;
import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.entity.Approval;
import com.drone.approval.entity.ConflictSegment;
import com.drone.approval.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/mission/{missionId}")
    public ApiResponse<List<Approval>> getApprovalsByMission(@PathVariable Long missionId) {
        return ApiResponse.success(approvalService.getApprovalsByMission(missionId));
    }

    @GetMapping("/mission/{missionId}/latest")
    public ApiResponse<Approval> getLatestApproval(@PathVariable Long missionId) {
        return ApiResponse.success(approvalService.getLatestApproval(missionId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Approval> getApprovalById(@PathVariable Long id) {
        return ApiResponse.success(approvalService.getApprovalById(id));
    }

    @GetMapping("/{id}/conflicts")
    public ApiResponse<List<ConflictSegment>> getConflictSegments(@PathVariable Long id) {
        return ApiResponse.success(approvalService.getConflictSegments(id));
    }

    @PostMapping
    public ApiResponse<Approval> createApproval(@Valid @RequestBody ApprovalRequest request) {
        String message = null;
        if (request.getDecision().equals("APPROVED")) {
            message = "审批通过";
        } else if (request.getDecision().equals("REJECTED")) {
            message = "审批拒绝";
        } else {
            message = "已退回修改";
        }
        return ApiResponse.success(message, approvalService.createApproval(request));
    }

    @PostMapping("/mission/{missionId}/recalculate-risk")
    public ApiResponse<ConflictCheckResult> recalculateRisk(@PathVariable Long missionId) {
        return ApiResponse.success("风险重新计算完成", approvalService.recalculateRisk(missionId));
    }
}
