package com.drone.approval.repository;

import com.drone.approval.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByMissionNumber(String missionNumber);

    List<Mission> findByPilotId(Long pilotId);

    List<Mission> findByDroneId(Long droneId);

    List<Mission> findByStatus(Mission.MissionStatus status);

    @Query("SELECT m FROM Mission m WHERE m.drone.id = :droneId " +
            "AND m.status IN ('APPROVED', 'IN_PROGRESS') " +
            "AND m.plannedStartTime < :endTime AND m.plannedEndTime > :startTime")
    List<Mission> findOverlappingMissionsForDrone(@Param("droneId") Long droneId,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("excludeMissionId") Long excludeMissionId);

    @Query("SELECT m FROM Mission m WHERE m.drone.id = :droneId " +
            "AND m.status IN ('APPROVED', 'IN_PROGRESS') " +
            "AND m.plannedStartTime < :endTime AND m.plannedEndTime > :startTime " +
            "AND m.id != :excludeMissionId")
    List<Mission> findOverlappingMissionsForDroneExcluding(@Param("droneId") Long droneId,
                                                            @Param("startTime") LocalDateTime startTime,
                                                            @Param("endTime") LocalDateTime endTime,
                                                            @Param("excludeMissionId") Long excludeMissionId);
}
