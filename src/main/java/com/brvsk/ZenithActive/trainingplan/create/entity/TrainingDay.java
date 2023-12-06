package com.brvsk.ZenithActive.trainingplan.create.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDay {

    private List<Exercise> exercises = new ArrayList<>();
}
