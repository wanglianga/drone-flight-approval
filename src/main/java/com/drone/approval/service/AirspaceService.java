package com.drone.approval.service;

import com.drone.approval.entity.Airspace;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.AirspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AirspaceService {

    private final AirspaceRepository airspaceRepository;

    @Transactional(readOnly = true)
    public List<Airspace> getAllAirspaces() {
        return airspaceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Airspace getAirspaceById(Long id) {
        return airspaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airspace", id));
    }

    @Transactional(readOnly = true)
    public Airspace getAirspaceByCode(String code) {
        return airspaceRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Airspace not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public List<Airspace> findAirspacesNearPoint(BigDecimal latitude, BigDecimal longitude, Double radiusMeters) {
        return airspaceRepository.findRestrictedAirspaces();
    }

    @Transactional(readOnly = true)
    public List<Airspace> findRestrictedAirspaces() {
        return airspaceRepository.findRestrictedAirspaces();
    }
}
