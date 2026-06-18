package com.drone.approval.repository;

import com.drone.approval.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByMissionId(Long missionId);

    Optional<Approval> findTopByMissionIdOrderByCreatedAtDesc(Long missionId);
}
