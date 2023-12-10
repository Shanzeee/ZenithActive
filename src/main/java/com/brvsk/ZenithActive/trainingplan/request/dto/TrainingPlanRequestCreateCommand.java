package com.brvsk.ZenithActive.trainingplan.request.dto;

import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequestMemberInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;

    @Valid
    @NotNull(message = "Member info cannot be null")
    private TrainingPlanRequestMemberInfo memberInfo;
}
