package com.brvsk.ZenithActive.trainingplan.request.dto;

import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import org.springframework.stereotype.Component;

@Component
public class TrainingPlanRequestMapper {

    public TrainingPlanRequestResponse toResponse(TrainingPlanRequest entity){
        return TrainingPlanRequestResponse
                .builder()
                .id(entity.getId())
                .memberId(entity.getMember().getUserId())
                .memberName(entity.getMember().getFirstName()+ " "+ entity.getMember().getLastName())
                .trainingPlanTarget(entity.getMemberInfo().getTarget())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public TrainingPlanRequestResponseDetailedInfo toResponseDetailedInfo(TrainingPlanRequest entity){
        return TrainingPlanRequestResponseDetailedInfo
                .builder()
                .id(entity.getId())
                .memberId(entity.getMember().getUserId())
                .memberName(entity.getMember().getFirstName()+ " "+ entity.getMember().getLastName())
                .memberInfo(entity.getMemberInfo())
                .build();
    }
}
