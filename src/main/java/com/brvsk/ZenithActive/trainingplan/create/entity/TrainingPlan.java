package com.brvsk.ZenithActive.trainingplan.create.entity;

import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlan {

    private Member member;
    private Instructor instructor;
    private TrainingPlanRequest trainingPlanRequest;
    private List<TrainingDay> trainingDays = new ArrayList<>();
}
