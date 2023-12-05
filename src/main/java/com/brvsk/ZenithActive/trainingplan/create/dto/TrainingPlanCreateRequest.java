package com.brvsk.ZenithActive.trainingplan.create.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TrainingPlanCreateRequest {

    private UUID memberId;
    private UUID instructorId;
    private List<TrainingDayCreateRequest> trainingDays;

}
