package com.drone.approval.service;

import com.drone.approval.dto.ApprovalRequest;
import com.drone.approval.dto.ConflictCheckResult;
import com.drone.approval.entity.Approval;
import com.drone.approval.entity.ConflictSegment;
import com.drone.approval.entity.FlightRoute;
import com.drone.approval.entity.Mission;
import com.drone.approval.exception.BusinessException;
import com.drone.approval.exception.ResourceNotFoundException;
import com.drone.approval.repository.ApprovalRepository;
import com.drone.approval.repository.ConflictSegmentRepository;
import com.drone.approval.repository.FlightRouteRepository;
import com.drone.approval.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApprovalService {

    private ApprovalRepository approvalRepository;
    private MissionRepository missionRepository;
    private ConflictSegmentRepository conflictSegmentRepository;
    private FlightRouteRepository flightRouteRepository;
    private ConflictDetectionService conflictDetectionService;

    public ApprovalService(ApprovalRepository approvalRepository, MissionRepository missionRepository,
                            ConflictSegmentRepository conflictSegmentRepository,
                            FlightRouteRepository flightRouteRepository,
                            ConflictDetectionService conflictDetectionService) {
        this.approvalRepository = approvalRepository;
        this.missionRepository = missionRepository;
        this.conflictSegmentRepository = conflictSegmentRepository;
        this.flightRouteRepository = flightRouteRepository;
        this.conflictDetectionService = conflictDetectionService;
    }

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
                && mission.getStatus() != Mission.MissionStatus.REJECTED
                && mission.getStatus() != Mission.MissionStatus.PENDING_REVIEW) {
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
                segment.setRestrictedMinAltitude(csr.getRestrictedMinAltitude());
                segment.setRestrictedMaxAltitude(csr.getRestrictedMaxAltitude());
                segment.setFlyableStartTime(csr.getFlyableStartTime());
                segment.setFlyableEndTime(csr.getFlyableEndTime());
                segment.setDetourSuggestion(csr.getDetourSuggestion());
                conflictSegments.add(segment);
            }
        }
        approval.setConflictSegments(conflictSegments);

        Approval savedApproval = approvalRepository.save(approval);

        if (decision == Approval.ApprovalDecision.APPROVED) {
            mission.setStatus(Mission.MissionStatus.APPROVED);

            LocalDateTime windowStart = request.getApprovedStartTime() != null
                    ? request.getApprovedStartTime()
                    : mission.getPlannedStartTime();
            LocalDateTime windowEnd = request.getApprovedEndTime() != null
                    ? request.getApprovedEndTime()
                    : mission.getPlannedEndTime();

            mission.setReportWindowStart(windowStart.minusMinutes(30));
            mission.setReportWindowEnd(windowStart.plusMinutes(15));

            mission.setLastRiskCalculatedAt(LocalDateTime.now());
        } else if (decision == Approval.ApprovalDecision.REJECTED) {
            mission.setStatus(Mission.MissionStatus.REJECTED);
        } else if (decision == Approval.ApprovalDecision.RETURNED_FOR_REVISION) {
            mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
        }
        missionRepository.save(mission);

        return savedApproval;
    }

    @Transactional
    public ConflictCheckResult recalculateRisk(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", missionId));

        if (mission.getStatus() != Mission.MissionStatus.APPROVED
                && mission.getStatus() != Mission.MissionStatus.SUBMITTED
                && mission.getStatus() != Mission.MissionStatus.PENDING_REVIEW) {
            throw new BusinessException("当前任务状态不支持重新计算风险，状态: " + mission.getStatus());
        }

        List<FlightRoute> routes = flightRouteRepository.findByMissionId(missionId);
        if (routes.isEmpty()) {
            throw new BusinessException("该任务没有航线信息，无法计算风险");
        }

        ConflictCheckResult result = conflictDetectionService.checkMissionConflicts(mission, routes.get(0));

        mission.setLastRiskCalculatedAt(LocalDateTime.now());
        missionRepository.save(mission);

        if (result.isHasConflict() && mission.getStatus() == Mission.MissionStatus.APPROVED) {
            mission.setStatus(Mission.MissionStatus.PENDING_REVIEW);
            missionRepository.save(mission);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<ConflictSegment> getConflictSegments(Long approvalId) {
        return conflictSegmentRepository.findByApprovalId(approvalId);
    }
}
