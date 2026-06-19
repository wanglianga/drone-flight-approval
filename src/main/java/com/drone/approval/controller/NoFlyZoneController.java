package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.NoFlyZoneRequest;
import com.drone.approval.entity.NoFlyZone;
import com.drone.approval.service.NoFlyZoneService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/no-fly-zones")
public class NoFlyZoneController {

    private NoFlyZoneService noFlyZoneService;

    public NoFlyZoneController(NoFlyZoneService noFlyZoneService) {
        this.noFlyZoneService = noFlyZoneService;
    }

    @GetMapping
    public ApiResponse<List<NoFlyZone>> getAllNoFlyZones() {
        return ApiResponse.success(noFlyZoneService.getAllNoFlyZones());
    }

    @GetMapping("/{id}")
    public ApiResponse<NoFlyZone> getNoFlyZoneById(@PathVariable Long id) {
        return ApiResponse.success(noFlyZoneService.getNoFlyZoneById(id));
    }

    @GetMapping("/active")
    public ApiResponse<List<NoFlyZone>> getActiveNoFlyZones(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkTime) {
        if (checkTime == null) {
            return ApiResponse.success(noFlyZoneService.getActiveNoFlyZones());
        }
        return ApiResponse.success(noFlyZoneService.getActiveNoFlyZones(checkTime));
    }

    @PostMapping
    public ApiResponse<NoFlyZone> createNoFlyZone(@Valid @RequestBody NoFlyZoneRequest request) {
        return ApiResponse.success("禁飞区创建成功", noFlyZoneService.createNoFlyZone(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<NoFlyZone> updateNoFlyZone(@PathVariable Long id, @Valid @RequestBody NoFlyZoneRequest request) {
        return ApiResponse.success("禁飞区更新成功", noFlyZoneService.updateNoFlyZone(id, request));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Void> cancelNoFlyZone(@PathVariable Long id) {
        noFlyZoneService.cancelNoFlyZone(id);
        return ApiResponse.success("禁飞区已取消", null);
    }
}
