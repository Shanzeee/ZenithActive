package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.pdf.PdfTrainingPlanGenerator;
import com.brvsk.ZenithActive.trainingplan.create.dto.ExerciseCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingDayCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.entity.Exercise;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingDay;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingPlan;
import com.brvsk.ZenithActive.trainingplan.request.TrainingPlanRequestRepository;
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
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final MemberRepository memberRepository;
    private final InstructorRepository instructorRepository;
    private final TrainingPlanRequestRepository trainingPlanRequestRepository;
    private final EmailSender emailSender;

    private static final String TRAINING_PLANS_FOLDER = "training_plans/";


    @Override
    public void createTrainingPlan(TrainingPlanCreateRequest request) {
        Member member = getMember(request.getMemberId());
        Instructor instructor = getInstructor(request.getInstructorId());
        TrainingPlanRequest trainingPlanRequest = getTrainingPlanRequest(request.getTrainingPlanRequestId());

        TrainingPlan trainingPlan = buildTrainingPlan(request, member, instructor, trainingPlanRequest);

        generateAndSaveTrainingPlanPdf(trainingPlan, request.getTrainingPlanRequestId());
        updateMemberTrainingPlanPaths(member, request.getMemberId(), request.getTrainingPlanRequestId());
        markTrainingPlanRequestAsCreated(trainingPlanRequest);
        sendTrainingPlanConfirmationEmail(member, instructor);
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

    private TrainingPlan buildTrainingPlan(TrainingPlanCreateRequest request, Member member, Instructor instructor, TrainingPlanRequest trainingPlanRequest) {
        return TrainingPlan.builder()
                .member(member)
                .instructor(instructor)
                .trainingPlanRequest(trainingPlanRequest)
                .trainingDays(mapTrainingDays(request.getTrainingDays()))
                .build();
    }

    private void generateAndSaveTrainingPlanPdf(TrainingPlan trainingPlan, UUID requestId) {
        PdfTrainingPlanGenerator.createTrainingPlanPdf(trainingPlan, TRAINING_PLANS_FOLDER, requestId.toString());
    }


    private List<TrainingDay> mapTrainingDays(List<TrainingDayCreateRequest> dayRequests) {
        return dayRequests.stream()
                .map(this::mapTrainingDay)
                .collect(Collectors.toList());
    }

    private TrainingDay mapTrainingDay(TrainingDayCreateRequest dayRequest) {
        TrainingDay trainingDay = new TrainingDay();
        trainingDay.setExercises(mapExercises(dayRequest.getExercises()));
        return trainingDay;
    }

    private List<Exercise> mapExercises(List<ExerciseCreateRequest> exerciseRequests) {
        return exerciseRequests.stream()
                .map(this::mapExercise)
                .collect(Collectors.toList());
    }

    private Exercise mapExercise(ExerciseCreateRequest exerciseRequest) {
        Exercise exercise = new Exercise();
        exercise.setExerciseType(exerciseRequest.getExerciseType());
        exercise.setSets(exerciseRequest.getSets());
        exercise.setReps(exerciseRequest.getReps());
        return exercise;
    }

    private void updateMemberTrainingPlanPaths(Member member, UUID memberId, UUID requestId) {
        String pathToTrainingPlanPdf = TRAINING_PLANS_FOLDER + memberId + "/" + requestId + ".pdf";
        member.getTrainingPlanPaths().add(pathToTrainingPlanPdf);
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
