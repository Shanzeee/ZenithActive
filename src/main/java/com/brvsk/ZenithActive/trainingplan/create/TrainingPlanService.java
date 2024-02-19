package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;

import java.util.UUID;

public interface TrainingPlanService {
    void createTrainingPlan(TrainingPlanCreateRequest request);

    byte[] getTrainingPlanPdf(UUID memberId, UUID trainingPlanRequestId);
}
