package com.drone.approval.service;

import com.drone.approval.dto.ApprovalRequest;
import com.drone.approval.entity.Approval;
import com.drone.approval.entity.ConflictSegment;
import com.drone.approval.entity.Mission;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.ApprovalRepository;
import com.drone.approval.repository.ConflictSegmentRepository;
import com.drone.approval.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final MissionRepository missionRepository;
    private final ConflictSegmentRepository conflictSegmentRepository;

    @Transactional(readOnly = true)
    public List<Approval> getApprovalsByMission(Long missionId) {
        return approvalRepository.findByMissionId(missionId);
    }

    @Transactional(readOnly = true)
    public Approval getLatestApproval(Long missionId) {
        return approvalRepository.findTopByMissionIdOrderByCreatedAtDesc(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("No approval found for mission: " + missionId));
    }

    @Transactional(readOnly = true)
    public Approval getApprovalById(Long id) {
        return approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval", id));
    }

    @Transactional
    public Approval createApproval(ApprovalRequest request) {
        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", request.getMissionId()));

        if (mission.getStatus() != Mission.MissionStatus.SUBMITTED
                && mission.getStatus() != Mission.MissionStatus.REJECTED) {
            throw new BusinessException("当前任务状态不允许审批，状态: " + mission.getStatus());
        }

        Approval.ApprovalDecision decision = Approval.ApprovalDecision.valueOf(request.getDecision());

        Approval approval = new Approval();
        approval.setMission(mission);
        approval.setApprover(request.getApprover());
        approval.setDecision(decision);
        approval.setGeneralComment(request.getGeneralComment());
        approval.setApprovedStartTime(request.getApprovedStartTime());
        approval.setApprovedEndTime(request.getApprovedEndTime());
        approval.setApprovedMaxAltitude(request.getApprovedMaxAltitude());

        List<ConflictSegment> conflictSegments = new ArrayList<>();
        if (request.getConflictSegments() != null) {
            for (ApprovalRequest.ConflictSegmentRequest csr : request.getConflictSegments()) {
                ConflictSegment segment = new ConflictSegment();
                segment.setApproval(approval);
                segment.setConflictType(ConflictSegment.ConflictType.valueOf(csr.getConflictType()));
                segment.setStartPointSequence(csr.getStartPointSequence());
                segment.setEndPointSequence(csr.getEndPointSequence());
                segment.setStartLatitude(csr.getStartLatitude());
                segment.setStartLongitude(csr.getStartLongitude());
                segment.setEndLatitude(csr.getEndLatitude());
                segment.setEndLongitude(csr.getEndLongitude());
                segment.setConflictAltitude(csr.getConflictAltitude());
                segment.setConflictZoneName(csr.getConflictZoneName());
                segment.setConflictZoneCode(csr.getConflictZoneCode());
                segment.setDescription(csr.getDescription());
                conflictSegments.add(segment);
            }
        }
        approval.setConflictSegments(conflictSegments);

        Approval savedApproval = approvalRepository.save(approval);

        if (decision == Approval.ApprovalDecision.APPROVED) {
            mission.setStatus(Mission.MissionStatus.APPROVED);
        } else if (decision == Approval.ApprovalDecision.REJECTED) {
            mission.setStatus(Mission.MissionStatus.REJECTED);
        }
        missionRepository.save(mission);

        return savedApproval;
    }

    @Transactional(readOnly = true)
    public List<ConflictSegment> getConflictSegments(Long approvalId) {
        return conflictSegmentRepository.findByApprovalId(approvalId);
    }
}
