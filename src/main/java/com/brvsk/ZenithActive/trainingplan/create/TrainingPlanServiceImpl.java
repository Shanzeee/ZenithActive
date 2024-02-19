package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.excpetion.MemberTrainingPlanRequestNotFound;
import com.brvsk.ZenithActive.excpetion.S3FileNotFound;
import com.brvsk.ZenithActive.s3.S3Buckets;
import com.brvsk.ZenithActive.s3.S3Service;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.pdf.PdfTrainingPlanGenerator;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingPlan;
import com.brvsk.ZenithActive.trainingplan.request.TrainingPlanRequestRepository;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final MemberRepository memberRepository;
    private final InstructorRepository instructorRepository;
    private final TrainingPlanRequestRepository trainingPlanRequestRepository;
    private final EmailSender emailSender;
    private final TrainingPlanMapper trainingPlanMapper;
    private final PdfTrainingPlanGenerator pdfTrainingPlanGenerator;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;


    @Override
    public void createTrainingPlan(TrainingPlanCreateRequest request) {
        Member member = getMember(request.getMemberId());
        Instructor instructor = getInstructor(request.getInstructorId());
        TrainingPlanRequest trainingPlanRequest = getTrainingPlanRequest(request.getTrainingPlanRequestId());

        TrainingPlan trainingPlan = trainingPlanMapper.mapToTrainingPlan(request, member, instructor, trainingPlanRequest);
        byte[] pdfBytes = pdfTrainingPlanGenerator.createTrainingPlanPdf(trainingPlan);

        String pdfKey = String.format("training-plans/%s/%s.pdf", request.getMemberId(), request.getTrainingPlanRequestId());
        s3Service.putObject(s3Buckets.getTrainingPlan(), pdfKey, pdfBytes);
        updateMemberTrainingPlanS3Keys(member, pdfKey);

        markTrainingPlanRequestAsCreated(trainingPlanRequest);
        sendTrainingPlanConfirmationEmail(member, instructor);
    }

    @Override
    public byte[] getTrainingPlanPdf(UUID memberId, UUID trainingPlanRequestId) {
        Member member = getMember(memberId);

        String pdfKey = String.format("training-plans/%s/%s.pdf", memberId, trainingPlanRequestId);


        if (!member.getTrainingPlanS3Keys().contains(pdfKey)) {
            throw new MemberTrainingPlanRequestNotFound(pdfKey);
        }

        try {
            return s3Service.getObject(s3Buckets.getTrainingPlan(), pdfKey);
        } catch (Exception e) {
            throw new S3FileNotFound(pdfKey);
        }
    }

    private Member getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private Instructor getInstructor(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new UserNotFoundException(instructorId));
    }

    private TrainingPlanRequest getTrainingPlanRequest(UUID requestId) {
        return trainingPlanRequestRepository.findById(requestId)
                .orElseThrow(() -> new TrainingPlanRequestNotFoundException(requestId));
    }

    private void updateMemberTrainingPlanS3Keys(Member member, String s3Key) {
        member.getTrainingPlanS3Keys().add(s3Key);
        memberRepository.save(member);
    }

    private void markTrainingPlanRequestAsCreated(TrainingPlanRequest trainingPlanRequest) {
        trainingPlanRequest.setCreated(true);
        trainingPlanRequestRepository.save(trainingPlanRequest);
    }

    private void sendTrainingPlanConfirmationEmail(Member member, Instructor instructor) {
        emailSender.sendTrainingPlanConfirmation(member, instructor);
    }

}
