package com.brvsk.ZenithActive.trainingplan.request.dto;

import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TrainingPlanRequestResponse {
    private UUID id;
    private UUID memberId;
    private String memberName;
    private LocalDateTime createdAt;
    private TrainingPlanTarget trainingPlanTarget;
}
