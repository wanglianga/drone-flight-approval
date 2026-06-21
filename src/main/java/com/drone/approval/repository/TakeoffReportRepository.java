package com.drone.approval.repository;

import com.drone.approval.entity.TakeoffReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TakeoffReportRepository extends JpaRepository<TakeoffReport, Long> {

    List<TakeoffReport> findByMissionId(Long missionId);

    @Query("SELECT t FROM TakeoffReport t WHERE t.mission.id = :missionId " +
            "AND t.actualTakeoffTime BETWEEN :startTime AND :endTime")
    List<TakeoffReport> findReportsInTimeRange(@Param("missionId") Long missionId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM TakeoffReport t WHERE t.mission.id = :missionId " +
            "AND t.status = 'REPORTED' " +
            "AND t.actualTakeoffTime BETWEEN :startTime AND :endTime")
    Optional<TakeoffReport> findDuplicateReport(@Param("missionId") Long missionId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM TakeoffReport t WHERE t.mission.id = :missionId AND t.status = 'REPORTED'")
    Optional<TakeoffReport> findActiveReportByMissionId(@Param("missionId") Long missionId);

    @Query("SELECT COUNT(t) > 0 FROM TakeoffReport t WHERE t.mission.id = :missionId AND t.status = 'REPORTED'")
    boolean hasActiveReport(@Param("missionId") Long missionId);
}
