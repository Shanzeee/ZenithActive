package com.brvsk.ZenithActive.trainingplan.create.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {

    private ExerciseType exerciseType;
    private int sets;
    private int reps;

    @Override
    public String toString() {
        if (exerciseType == ExerciseType.REST_DAY){
            return "REST DAY";
        }
        return  exerciseType + "\n" +
                "sets: " + sets + "\n" +
                "reps: " + reps + "\n";
    }
}
