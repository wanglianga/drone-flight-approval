package com.drone.approval.repository;

import com.drone.approval.entity.ConflictSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConflictSegmentRepository extends JpaRepository<ConflictSegment, Long> {

    List<ConflictSegment> findByApprovalId(Long approvalId);
}
