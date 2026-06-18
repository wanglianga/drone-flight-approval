package com.drone.approval.service;

import com.drone.approval.dto.PilotRequest;
import com.drone.approval.entity.Pilot;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PilotService {

    private final PilotRepository pilotRepository;

    @Transactional(readOnly = true)
    public List<Pilot> getAllPilots() {
        return pilotRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pilot getPilotById(Long id) {
        return pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot", id));
    }

    @Transactional(readOnly = true)
    public Pilot getPilotByLicenseNumber(String licenseNumber) {
        return pilotRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with license: " + licenseNumber));
    }

    @Transactional
    public Pilot createPilot(PilotRequest request) {
        if (pilotRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BusinessException("该飞手执照编号已存在");
        }

        Pilot pilot = new Pilot();
        pilot.setLicenseNumber(request.getLicenseNumber());
        pilot.setName(request.getName());
        pilot.setIdCard(request.getIdCard());
        pilot.setPhone(request.getPhone());
        pilot.setEmail(request.getEmail());
        pilot.setLicenseIssueDate(request.getLicenseIssueDate());
        pilot.setLicenseExpiryDate(request.getLicenseExpiryDate());
        pilot.setLevel(Pilot.PilotLevel.valueOf(request.getLevel()));
        pilot.setQualificationFileUrl(request.getQualificationFileUrl());

        if (request.getLicenseExpiryDate().isBefore(LocalDate.now())) {
            pilot.setStatus(Pilot.PilotStatus.EXPIRED);
        }

        return pilotRepository.save(pilot);
    }

    @Transactional
    public Pilot updatePilot(Long id, PilotRequest request) {
        Pilot pilot = getPilotById(id);

        if (!pilot.getLicenseNumber().equals(request.getLicenseNumber())
                && pilotRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BusinessException("该飞手执照编号已存在");
        }

        pilot.setLicenseNumber(request.getLicenseNumber());
        pilot.setName(request.getName());
        pilot.setIdCard(request.getIdCard());
        pilot.setPhone(request.getPhone());
        pilot.setEmail(request.getEmail());
        pilot.setLicenseIssueDate(request.getLicenseIssueDate());
        pilot.setLicenseExpiryDate(request.getLicenseExpiryDate());
        pilot.setLevel(Pilot.PilotLevel.valueOf(request.getLevel()));
        pilot.setQualificationFileUrl(request.getQualificationFileUrl());

        if (request.getLicenseExpiryDate().isBefore(LocalDate.now())) {
            pilot.setStatus(Pilot.PilotStatus.EXPIRED);
        } else if (pilot.getStatus() == Pilot.PilotStatus.EXPIRED) {
            pilot.setStatus(Pilot.PilotStatus.ACTIVE);
        }

        return pilotRepository.save(pilot);
    }

    @Transactional
    public void deletePilot(Long id) {
        Pilot pilot = getPilotById(id);
        pilot.setStatus(Pilot.PilotStatus.SUSPENDED);
        pilotRepository.save(pilot);
    }
}
