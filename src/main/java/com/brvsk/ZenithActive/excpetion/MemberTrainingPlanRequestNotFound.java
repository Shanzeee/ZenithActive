package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberTrainingPlanRequestNotFound extends RuntimeException{
    public MemberTrainingPlanRequestNotFound(String trainingPlanRequest) {
        super("Training plan request: "+trainingPlanRequest+ " not found");
    }
}