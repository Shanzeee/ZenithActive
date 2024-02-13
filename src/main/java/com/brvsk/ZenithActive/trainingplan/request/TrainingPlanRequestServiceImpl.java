package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.loyalty.LoyaltyPointsCreateRequest;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsType;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestMapper;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponseDetailedInfo;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanRequestServiceImpl implements TrainingPlanRequestService {

    private final TrainingPlanRequestRepository trainingPlanRequestRepository;
    private final MemberRepository memberRepository;
    private final TrainingPlanRequestMapper trainingPlanRequestMapper;
    private final EmailSender emailSender;
    private final LoyaltyPointsService loyaltyPointsService;

    @Override
    public void createTrainingPlanRequest(TrainingPlanRequestCreateCommand command) {
        Member member = getMember(command.getMemberId());

        TrainingPlanRequest trainingPlanRequest = buildTrainingPlanRequest(member, command);

        saveTrainingPlanRequest(trainingPlanRequest);

        LoyaltyPointsCreateRequest loyaltyPointsCreateRequest = buildLoyaltyPointsCreateRequest(command.getMemberId());
        addLoyaltyPoints(loyaltyPointsCreateRequest);

        sendTrainingPlanRequestConfirmationEmail(member);
    }

    @Override
    public List<TrainingPlanRequestResponse> getPendingTrainingPlanRequests() {
        return trainingPlanRequestRepository.findByCreatedOrderByCreatedAtDesc(false)
                .stream()
                .map(trainingPlanRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TrainingPlanRequestResponseDetailedInfo getTrainingPlanRequestById(UUID id) {
        return trainingPlanRequestRepository
                .findById(id)
                .map(trainingPlanRequestMapper::toResponseDetailedInfo)
                .orElseThrow(() -> new TrainingPlanRequestNotFoundException(id));
    }

    private Member getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private TrainingPlanRequest buildTrainingPlanRequest(Member member, TrainingPlanRequestCreateCommand command) {
        return TrainingPlanRequest.builder()
                .id(UUID.randomUUID())
                .created(false)
                .member(member)
                .memberInfo(command.getMemberInfo())
                .build();
    }

    private void saveTrainingPlanRequest(TrainingPlanRequest trainingPlanRequest) {
        trainingPlanRequestRepository.save(trainingPlanRequest);
    }

    private void addLoyaltyPoints(LoyaltyPointsCreateRequest loyaltyPointsCreateRequest) {
        loyaltyPointsService.addLoyaltyPoints(loyaltyPointsCreateRequest);
    }

    private void sendTrainingPlanRequestConfirmationEmail(Member member) {
        emailSender.sendTrainingPlanRequestConfirmation(member);
    }

    private LoyaltyPointsCreateRequest buildLoyaltyPointsCreateRequest(UUID memberId){
        return LoyaltyPointsCreateRequest
                .builder()
                .loyaltyPointsType(LoyaltyPointsType.MEMBERSHIP)
                .pointsAmount(300)
                .memberId(memberId)
                .build();
    }
}
