package com.brvsk.ZenithActive.trainingplan.request.dto;

import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequestMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TrainingPlanRequestResponseDetailedInfo {
    private UUID id;
    private UUID memberId;
    private String memberName;
    private TrainingPlanRequestMemberInfo memberInfo;
}
