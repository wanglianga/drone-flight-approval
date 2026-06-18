package com.drone.approval.repository;

import com.drone.approval.entity.FlightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightLogRepository extends JpaRepository<FlightLog, Long> {

    List<FlightLog> findByMissionId(Long missionId);

    Optional<FlightLog> findTopByMissionIdOrderByCreatedAtDesc(Long missionId);
}
