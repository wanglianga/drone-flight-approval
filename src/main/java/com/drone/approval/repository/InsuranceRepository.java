package com.drone.approval.repository;

import com.drone.approval.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    Optional<Insurance> findByPolicyNumber(String policyNumber);

    List<Insurance> findByDroneId(Long droneId);

    @Query("SELECT i FROM Insurance i WHERE i.drone.id = :droneId AND i.status = 'VALID' " +
            "AND i.effectiveDate <= :checkTime AND i.expiryDate >= :checkTime")
    Optional<Insurance> findValidInsuranceByDroneId(@Param("droneId") Long droneId,
                                                     @Param("checkTime") LocalDateTime checkTime);
}
