package com.brvsk.ZenithActive.trainingplan.request.dto;

import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequestMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TrainingPlanRequestCreateCommand {
    private UUID memberId;
    private TrainingPlanRequestMemberInfo memberInfo;
}
