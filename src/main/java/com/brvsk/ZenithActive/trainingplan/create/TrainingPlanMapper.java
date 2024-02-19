package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.trainingplan.create.dto.ExerciseCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingDayCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import com.brvsk.ZenithActive.trainingplan.create.entity.Exercise;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingDay;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingPlan;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingPlanMapper {

    public TrainingPlan mapToTrainingPlan(TrainingPlanCreateRequest request, Member member, Instructor instructor, TrainingPlanRequest trainingPlanRequest) {
        List<TrainingDay> trainingDays = request.getTrainingDays().stream()
                .map(this::mapTrainingDay)
                .collect(Collectors.toList());

        return new TrainingPlan(member, instructor, trainingPlanRequest, trainingDays);
    }

    private TrainingDay mapTrainingDay(TrainingDayCreateRequest dayRequest) {
        List<Exercise> exercises = dayRequest.getExercises().stream()
                .map(this::mapExercise)
                .collect(Collectors.toList());

        return new TrainingDay(exercises);
    }

    private Exercise mapExercise(ExerciseCreateRequest exerciseRequest) {
        return new Exercise(exerciseRequest.getExerciseType(), exerciseRequest.getSets(), exerciseRequest.getReps());
    }
}
