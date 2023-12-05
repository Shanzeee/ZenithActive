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
        return  "exercise: " + exerciseType + "\n" +
                "sets: " + sets + "\n" +
                "reps: " + reps + "\n";
    }
}
