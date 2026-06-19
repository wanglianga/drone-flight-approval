package com.drone.approval.controller;

import com.drone.approval.common.ApiResponse;
import com.drone.approval.entity.Airspace;
import com.drone.approval.service.AirspaceService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/airspaces")
public class AirspaceController {

    private AirspaceService airspaceService;

    public AirspaceController(AirspaceService airspaceService) {
        this.airspaceService = airspaceService;
    }

    @GetMapping
    public ApiResponse<List<Airspace>> getAllAirspaces() {
        return ApiResponse.success(airspaceService.getAllAirspaces());
    }

    @GetMapping("/{id}")
    public ApiResponse<Airspace> getAirspaceById(@PathVariable Long id) {
        return ApiResponse.success(airspaceService.getAirspaceById(id));
    }

    @GetMapping("/code/{code}")
    public ApiResponse<Airspace> getAirspaceByCode(@PathVariable String code) {
        return ApiResponse.success(airspaceService.getAirspaceByCode(code));
    }

    @GetMapping("/nearby")
    public ApiResponse<List<Airspace>> findAirspacesNearPoint(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "5000") Double radiusMeters) {
        return ApiResponse.success(airspaceService.findAirspacesNearPoint(latitude, longitude, radiusMeters));
    }

    @GetMapping("/restricted")
    public ApiResponse<List<Airspace>> findRestrictedAirspaces() {
        return ApiResponse.success(airspaceService.findRestrictedAirspaces());
    }
}
