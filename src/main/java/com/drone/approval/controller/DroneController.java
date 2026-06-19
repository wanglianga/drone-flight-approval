package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.dto.DroneRequest;
import com.drone.approval.entity.Drone;
import com.drone.approval.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
public class DroneController {

    private DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping
    public ApiResponse<List<Drone>> getAllDrones() {
        return ApiResponse.success(droneService.getAllDrones());
    }

    @GetMapping("/{id}")
    public ApiResponse<Drone> getDroneById(@PathVariable Long id) {
        return ApiResponse.success(droneService.getDroneById(id));
    }

    @GetMapping("/serial/{serialNumber}")
    public ApiResponse<Drone> getDroneBySerialNumber(@PathVariable String serialNumber) {
        return ApiResponse.success(droneService.getDroneBySerialNumber(serialNumber));
    }

    @PostMapping
    public ApiResponse<Drone> createDrone(@Valid @RequestBody DroneRequest request) {
        return ApiResponse.success("无人机创建成功", droneService.createDrone(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Drone> updateDrone(@PathVariable Long id, @Valid @RequestBody DroneRequest request) {
        return ApiResponse.success("无人机更新成功", droneService.updateDrone(id, request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Drone> updateDroneStatus(@PathVariable Long id, @RequestParam Drone.DroneStatus status) {
        return ApiResponse.success("无人机状态更新成功", droneService.updateDroneStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDrone(@PathVariable Long id) {
        droneService.deleteDrone(id);
        return ApiResponse.success("无人机已退役", null);
    }
}
