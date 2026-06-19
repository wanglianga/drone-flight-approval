package com.drone.approval.service;

import com.drone.approval.dto.DroneRequest;
import com.drone.approval.entity.Drone;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.DroneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DroneService {

    private DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Transactional(readOnly = true)
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Drone getDroneById(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone", id));
    }

    @Transactional(readOnly = true)
    public Drone getDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found with serial: " + serialNumber));
    }

    @Transactional
    public Drone createDrone(DroneRequest request) {
        if (droneRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new BusinessException("该无人机序列号已存在");
        }

        Drone drone = new Drone();
        drone.setSerialNumber(request.getSerialNumber());
        drone.setModel(request.getModel());
        drone.setManufacturer(request.getManufacturer());
        drone.setMaxTakeoffWeight(request.getMaxTakeoffWeight());
        drone.setMaxFlightAltitude(request.getMaxFlightAltitude());
        drone.setMaxFlightTime(request.getMaxFlightTime());
        drone.setBatteryType(request.getBatteryType());
        drone.setBatteryCount(request.getBatteryCount());
        drone.setRegistrationFileUrl(request.getRegistrationFileUrl());

        return droneRepository.save(drone);
    }

    @Transactional
    public Drone updateDrone(Long id, DroneRequest request) {
        Drone drone = getDroneById(id);

        if (!drone.getSerialNumber().equals(request.getSerialNumber())
                && droneRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new BusinessException("该无人机序列号已存在");
        }

        drone.setSerialNumber(request.getSerialNumber());
        drone.setModel(request.getModel());
        drone.setManufacturer(request.getManufacturer());
        drone.setMaxTakeoffWeight(request.getMaxTakeoffWeight());
        drone.setMaxFlightAltitude(request.getMaxFlightAltitude());
        drone.setMaxFlightTime(request.getMaxFlightTime());
        drone.setBatteryType(request.getBatteryType());
        drone.setBatteryCount(request.getBatteryCount());
        drone.setRegistrationFileUrl(request.getRegistrationFileUrl());

        return droneRepository.save(drone);
    }

    @Transactional
    public void deleteDrone(Long id) {
        Drone drone = getDroneById(id);
        drone.setStatus(Drone.DroneStatus.RETIRED);
        droneRepository.save(drone);
    }

    @Transactional
    public Drone updateDroneStatus(Long id, Drone.DroneStatus status) {
        Drone drone = getDroneById(id);
        drone.setStatus(status);
        return droneRepository.save(drone);
    }
}
