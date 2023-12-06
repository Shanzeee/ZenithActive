package com.brvsk.ZenithActive.trainingplan.create.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingDayCreateRequest {
    private List<ExerciseCreateRequest> exercises;
}
