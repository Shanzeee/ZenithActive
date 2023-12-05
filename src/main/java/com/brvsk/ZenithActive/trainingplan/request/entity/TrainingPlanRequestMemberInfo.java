package com.brvsk.ZenithActive.trainingplan.request.entity;

import com.brvsk.ZenithActive.user.Gender;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TrainingPlanRequestMemberInfo {
    private Gender gender;
    private int age;
    private TrainingPlanTarget target;
    private int bodyFatLevel;
    private TrainingPlanDiet diet;
    private int height;
    private int currentWeight;
    private int targetWeight;
}
