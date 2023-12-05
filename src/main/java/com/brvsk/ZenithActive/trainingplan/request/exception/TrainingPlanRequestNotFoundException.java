package com.brvsk.ZenithActive.trainingplan.request.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrainingPlanRequestNotFoundException extends RuntimeException{
    public TrainingPlanRequestNotFoundException(final UUID id) {
        super("TrainingPlanRequest with id "+id+" not found");
    }
}
