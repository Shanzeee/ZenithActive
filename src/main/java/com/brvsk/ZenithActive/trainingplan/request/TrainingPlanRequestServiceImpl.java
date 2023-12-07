package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberRepository;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestMapper;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponseDetailedInfo;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
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

    @Override
    public void createTrainingPlanRequest(TrainingPlanRequestCreateCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(command.getMemberId()));

        TrainingPlanRequest trainingPlanRequest = TrainingPlanRequest.builder()
                .id(UUID.randomUUID())
                .created(false)
                .member(member)
                .memberInfo(command.getMemberInfo())
                .build();

        trainingPlanRequestRepository.save(trainingPlanRequest);

        emailSender.sendTrainingPlanRequestConfirmation(member);
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
}
