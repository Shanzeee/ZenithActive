package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final MemberRepository memberRepository;
    private final InstructorRepository instructorRepository;
    private final TrainingPlanRequestRepository trainingPlanRequestRepository;

    @Override
    public void createTrainingPlan(TrainingPlanCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(request.getMemberId()));

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new UserNotFoundException(request.getInstructorId()));

        TrainingPlanRequest trainingPlanRequest = trainingPlanRequestRepository.findById(request.getTrainingPlanRequestId())
                .orElseThrow(() -> new TrainingPlanRequestNotFoundException(request.getTrainingPlanRequestId()));

        TrainingPlan trainingPlan = TrainingPlan.builder()
                .member(member)
                .instructor(instructor)
                .trainingPlanRequest(trainingPlanRequest)
                .trainingDays(mapTrainingDays(request.getTrainingDays()))
                .build();

        PdfTrainingPlanGenerator.createTrainingPlanPdf(trainingPlan, "training_plans", trainingPlan.getTrainingPlanRequest().getId().toString());
        String pathToTrainingPlanPdf = "training_plans/"
                + request.getMemberId() + "/"
                + request.getTrainingPlanRequestId() + ".pdf";
        member.getTrainingPlanPaths().add(pathToTrainingPlanPdf);
        memberRepository.save(member);

        trainingPlanRequest.setCreated(true);
        trainingPlanRequestRepository.save(trainingPlanRequest);
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

}
