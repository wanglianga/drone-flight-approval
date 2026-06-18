package com.drone.approval.repository;

import com.drone.approval.entity.FlightRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

    List<FlightRoute> findByMissionId(Long missionId);
}
