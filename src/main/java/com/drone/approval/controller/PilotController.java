package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.PilotRequest;
import com.drone.approval.entity.Pilot;
import com.drone.approval.service.PilotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pilots")
public class PilotController {

    private PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping
    public ApiResponse<List<Pilot>> getAllPilots() {
        return ApiResponse.success(pilotService.getAllPilots());
    }

    @GetMapping("/{id}")
    public ApiResponse<Pilot> getPilotById(@PathVariable Long id) {
        return ApiResponse.success(pilotService.getPilotById(id));
    }

    @GetMapping("/license/{licenseNumber}")
    public ApiResponse<Pilot> getPilotByLicenseNumber(@PathVariable String licenseNumber) {
        return ApiResponse.success(pilotService.getPilotByLicenseNumber(licenseNumber));
    }

    @PostMapping
    public ApiResponse<Pilot> createPilot(@Valid @RequestBody PilotRequest request) {
        return ApiResponse.success("飞手创建成功", pilotService.createPilot(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Pilot> updatePilot(@PathVariable Long id, @Valid @RequestBody PilotRequest request) {
        return ApiResponse.success("飞手更新成功", pilotService.updatePilot(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePilot(@PathVariable Long id) {
        pilotService.deletePilot(id);
        return ApiResponse.success("飞手已停用", null);
    }
}
