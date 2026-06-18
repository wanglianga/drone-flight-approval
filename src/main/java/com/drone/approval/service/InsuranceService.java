package com.drone.approval.service;

import com.drone.approval.dto.InsuranceRequest;
import com.drone.approval.entity.Drone;
import com.drone.approval.entity.Insurance;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.DroneRepository;
import com.drone.approval.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final DroneRepository droneRepository;

    @Transactional(readOnly = true)
    public List<Insurance> getAllInsurance() {
        return insuranceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Insurance getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance", id));
    }

    @Transactional(readOnly = true)
    public List<Insurance> getInsuranceByDroneId(Long droneId) {
        return insuranceRepository.findByDroneId(droneId);
    }

    @Transactional(readOnly = true)
    public Insurance getValidInsurance(Long droneId, LocalDateTime checkTime) {
        return insuranceRepository.findValidInsuranceByDroneId(droneId, checkTime)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No valid insurance found for drone " + droneId + " at " + checkTime));
    }

    @Transactional
    public Insurance createInsurance(InsuranceRequest request) {
        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone", request.getDroneId()));

        if (insuranceRepository.findByPolicyNumber(request.getPolicyNumber()).isPresent()) {
            throw new BusinessException("该保单号已存在");
        }

        Insurance insurance = new Insurance();
        insurance.setDrone(drone);
        insurance.setPolicyNumber(request.getPolicyNumber());
        insurance.setInsuranceCompany(request.getInsuranceCompany());
        insurance.setCoverageAmount(request.getCoverageAmount());
        insurance.setEffectiveDate(request.getEffectiveDate());
        insurance.setExpiryDate(request.getExpiryDate());
        insurance.setPolicyFileUrl(request.getPolicyFileUrl());

        if (request.getExpiryDate().isBefore(LocalDateTime.now())) {
            insurance.setStatus(Insurance.InsuranceStatus.EXPIRED);
        }

        return insuranceRepository.save(insurance);
    }

    @Transactional
    public Insurance updateInsurance(Long id, InsuranceRequest request) {
        Insurance insurance = getInsuranceById(id);

        if (!insurance.getPolicyNumber().equals(request.getPolicyNumber())
                && insuranceRepository.findByPolicyNumber(request.getPolicyNumber()).isPresent()) {
            throw new BusinessException("该保单号已存在");
        }

        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone", request.getDroneId()));

        insurance.setDrone(drone);
        insurance.setPolicyNumber(request.getPolicyNumber());
        insurance.setInsuranceCompany(request.getInsuranceCompany());
        insurance.setCoverageAmount(request.getCoverageAmount());
        insurance.setEffectiveDate(request.getEffectiveDate());
        insurance.setExpiryDate(request.getExpiryDate());
        insurance.setPolicyFileUrl(request.getPolicyFileUrl());

        if (request.getExpiryDate().isBefore(LocalDateTime.now())) {
            insurance.setStatus(Insurance.InsuranceStatus.EXPIRED);
        } else if (insurance.getStatus() == Insurance.InsuranceStatus.EXPIRED) {
            insurance.setStatus(Insurance.InsuranceStatus.VALID);
        }

        return insuranceRepository.save(insurance);
    }

    @Transactional
    public void cancelInsurance(Long id) {
        Insurance insurance = getInsuranceById(id);
        insurance.setStatus(Insurance.InsuranceStatus.CANCELLED);
        insuranceRepository.save(insurance);
    }
}
