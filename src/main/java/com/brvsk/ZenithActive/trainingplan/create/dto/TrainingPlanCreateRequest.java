package com.brvsk.ZenithActive.trainingplan.create.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TrainingPlanCreateRequest {

    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;

    @NotNull(message = "Instructor ID cannot be null")
    private UUID instructorId;

    @NotNull(message = "Training Plan Request ID cannot be null")
    private UUID trainingPlanRequestId;
    private List<TrainingDayCreateRequest> trainingDays;

}
