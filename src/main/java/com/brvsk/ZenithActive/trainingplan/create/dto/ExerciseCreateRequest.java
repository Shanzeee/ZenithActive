package com.brvsk.ZenithActive.trainingplan.create.dto;

import com.brvsk.ZenithActive.trainingplan.create.entity.ExerciseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseCreateRequest {

    private ExerciseType exerciseType;
    private int sets;
    private int reps;

}
