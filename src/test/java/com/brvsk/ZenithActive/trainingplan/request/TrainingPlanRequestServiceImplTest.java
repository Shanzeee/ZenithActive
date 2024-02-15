package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestMapper;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequestMemberInfo;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanTarget;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainingPlanRequestServiceImplTest {

    @Mock
    private TrainingPlanRequestRepository trainingPlanRequestRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TrainingPlanRequestMapper trainingPlanRequestMapper;
    @Mock
    private EmailSender emailSender;
    @Mock
    private LoyaltyPointsService loyaltyPointsService;

    @InjectMocks
    private TrainingPlanRequestServiceImpl trainingPlanRequestService;


    @Test
    void createTrainingPlanRequest_Success() {
        UUID memberId = UUID.randomUUID();
        TrainingPlanRequestCreateCommand command = new TrainingPlanRequestCreateCommand(memberId, new TrainingPlanRequestMemberInfo());
        Member member = new Member();
        TrainingPlanRequest trainingPlanRequest = new TrainingPlanRequest();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(trainingPlanRequestRepository.save(any(TrainingPlanRequest.class))).thenReturn(trainingPlanRequest);

        // Execute the test method
        trainingPlanRequestService.createTrainingPlanRequest(command);

        // Verify interactions
        verify(memberRepository).findById(memberId);
        verify(trainingPlanRequestRepository).save(any(TrainingPlanRequest.class));
        verify(loyaltyPointsService).addLoyaltyPoints(any());
        verify(emailSender).sendTrainingPlanRequestConfirmation(member);
    }

    @Test
    void getPendingTrainingPlanRequests_ReturnsList() {
        when(trainingPlanRequestRepository.findByCreatedOrderByCreatedAtDesc(false))
                .thenReturn(List.of(new TrainingPlanRequest(), new TrainingPlanRequest()));
        when(trainingPlanRequestMapper.toResponse(any(TrainingPlanRequest.class)))
                .thenReturn(new TrainingPlanRequestResponse(UUID.randomUUID(),UUID.randomUUID(),"somename",LocalDateTime.now(), TrainingPlanTarget.LOSE_WEIGHT));

        List<TrainingPlanRequestResponse> responses = trainingPlanRequestService.getPendingTrainingPlanRequests();

        Assertions.assertEquals(2, responses.size());
        verify(trainingPlanRequestRepository).findByCreatedOrderByCreatedAtDesc(false);
        verify(trainingPlanRequestMapper, times(2)).toResponse(any(TrainingPlanRequest.class));
    }

    @Test
    void getTrainingPlanRequestById_NotFound() {
        UUID requestId = UUID.randomUUID();
        when(trainingPlanRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(TrainingPlanRequestNotFoundException.class, () -> trainingPlanRequestService.getTrainingPlanRequestById(requestId));

        verify(trainingPlanRequestRepository).findById(requestId);
    }

}