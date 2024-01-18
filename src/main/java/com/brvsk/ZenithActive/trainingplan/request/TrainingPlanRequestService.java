package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponseDetailedInfo;

import java.util.List;
import java.util.UUID;

public interface TrainingPlanRequestService {
    void createTrainingPlanRequest(TrainingPlanRequestCreateCommand command);
    List<TrainingPlanRequestResponse> getPendingTrainingPlanRequests();
    TrainingPlanRequestResponseDetailedInfo getTrainingPlanRequestById(UUID id);
}
