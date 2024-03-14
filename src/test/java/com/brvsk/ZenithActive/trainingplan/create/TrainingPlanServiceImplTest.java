package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.pdf.PdfTrainingPlanGenerator;
import com.brvsk.ZenithActive.s3.S3Buckets;
import com.brvsk.ZenithActive.s3.S3Service;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import com.brvsk.ZenithActive.trainingplan.request.TrainingPlanRequestRepository;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequestMemberInfo;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainingPlanServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private TrainingPlanRequestRepository trainingPlanRequestRepository;
    @Mock
    private EmailSender emailSender;
    @Mock
    private PdfTrainingPlanGenerator pdfTrainingPlanGenerator;
    @Mock
    private TrainingPlanMapper trainingPlanMapper;
    @Mock
    private S3Buckets s3Buckets;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private TrainingPlanServiceImpl trainingPlanService;

    @Test
    void createTrainingPlan_Success() {
        UUID memberId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        // Given
        TrainingPlanCreateRequest request = mock(TrainingPlanCreateRequest.class);
        when(request.getMemberId()).thenReturn(memberId);
        when(request.getInstructorId()).thenReturn(instructorId);
        when(request.getTrainingPlanRequestId()).thenReturn(requestId);

        Member member = new Member();
        member.setUserId(memberId);
        Instructor instructor = mock(Instructor.class);
        TrainingPlanRequest trainingPlanRequest = new TrainingPlanRequest(
                UUID.randomUUID(),
                false,
                member,
                new TrainingPlanRequestMemberInfo(),
                LocalDateTime.now()
        );

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(trainingPlanRequestRepository.findById(requestId)).thenReturn(Optional.of(trainingPlanRequest));
        doNothing().when(emailSender).sendTrainingPlanConfirmation(any(Member.class), any(Instructor.class));

        // When
        trainingPlanService.createTrainingPlan(request);

        // Then
        verify(memberRepository).findById(memberId);
        verify(instructorRepository).findById(instructorId);
        verify(trainingPlanRequestRepository).findById(requestId);
        verify(memberRepository).save(any(Member.class));
        verify(trainingPlanRequestRepository).save(any(TrainingPlanRequest.class));
        verify(emailSender).sendTrainingPlanConfirmation(any(Member.class), any(Instructor.class));
    }

}
