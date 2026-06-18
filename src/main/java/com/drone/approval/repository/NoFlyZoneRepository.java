package com.drone.approval.repository;

import com.drone.approval.entity.NoFlyZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoFlyZoneRepository extends JpaRepository<NoFlyZone, Long> {

    @Query("SELECT n FROM NoFlyZone n WHERE n.status = 'ACTIVE' " +
            "AND (n.effectiveFrom IS NULL OR n.effectiveFrom <= :checkTime) " +
            "AND (n.effectiveTo IS NULL OR n.effectiveTo >= :checkTime)")
    List<NoFlyZone> findActiveNoFlyZones(@Param("checkTime") LocalDateTime checkTime);

    List<NoFlyZone> findByStatus(NoFlyZone.NoFlyZoneStatus status);
}
