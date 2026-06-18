package com.drone.approval.service;

import com.drone.approval.dto.NoFlyZoneRequest;
import com.drone.approval.entity.NoFlyZone;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.NoFlyZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoFlyZoneService {

    private final NoFlyZoneRepository noFlyZoneRepository;

    @Transactional(readOnly = true)
    public List<NoFlyZone> getAllNoFlyZones() {
        return noFlyZoneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NoFlyZone getNoFlyZoneById(Long id) {
        return noFlyZoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NoFlyZone", id));
    }

    @Transactional(readOnly = true)
    public List<NoFlyZone> getActiveNoFlyZones(LocalDateTime checkTime) {
        return noFlyZoneRepository.findActiveNoFlyZones(checkTime);
    }

    @Transactional(readOnly = true)
    public List<NoFlyZone> getActiveNoFlyZones() {
        return noFlyZoneRepository.findActiveNoFlyZones(LocalDateTime.now());
    }

    @Transactional
    public NoFlyZone createNoFlyZone(NoFlyZoneRequest request) {
        NoFlyZone zone = new NoFlyZone();
        zone.setName(request.getName());
        zone.setType(NoFlyZone.NoFlyZoneType.valueOf(request.getType()));
        zone.setCenterLatitude(request.getCenterLatitude());
        zone.setCenterLongitude(request.getCenterLongitude());
        zone.setRadiusMeters(request.getRadiusMeters());
        zone.setMinAltitudeMeters(request.getMinAltitudeMeters());
        zone.setMaxAltitudeMeters(request.getMaxAltitudeMeters());
        zone.setEffectiveFrom(request.getEffectiveFrom());
        zone.setEffectiveTo(request.getEffectiveTo());
        zone.setReason(request.getReason());

        if (request.getEffectiveTo() != null && request.getEffectiveTo().isBefore(LocalDateTime.now())) {
            zone.setStatus(NoFlyZone.NoFlyZoneStatus.EXPIRED);
        }

        return noFlyZoneRepository.save(zone);
    }

    @Transactional
    public NoFlyZone updateNoFlyZone(Long id, NoFlyZoneRequest request) {
        NoFlyZone zone = getNoFlyZoneById(id);

        zone.setName(request.getName());
        zone.setType(NoFlyZone.NoFlyZoneType.valueOf(request.getType()));
        zone.setCenterLatitude(request.getCenterLatitude());
        zone.setCenterLongitude(request.getCenterLongitude());
        zone.setRadiusMeters(request.getRadiusMeters());
        zone.setMinAltitudeMeters(request.getMinAltitudeMeters());
        zone.setMaxAltitudeMeters(request.getMaxAltitudeMeters());
        zone.setEffectiveFrom(request.getEffectiveFrom());
        zone.setEffectiveTo(request.getEffectiveTo());
        zone.setReason(request.getReason());

        if (request.getEffectiveTo() != null && request.getEffectiveTo().isBefore(LocalDateTime.now())) {
            zone.setStatus(NoFlyZone.NoFlyZoneStatus.EXPIRED);
        } else if (zone.getStatus() == NoFlyZone.NoFlyZoneStatus.EXPIRED) {
            zone.setStatus(NoFlyZone.NoFlyZoneStatus.ACTIVE);
        }

        return noFlyZoneRepository.save(zone);
    }

    @Transactional
    public void cancelNoFlyZone(Long id) {
        NoFlyZone zone = getNoFlyZoneById(id);
        zone.setStatus(NoFlyZone.NoFlyZoneStatus.CANCELLED);
        noFlyZoneRepository.save(zone);
    }
}
