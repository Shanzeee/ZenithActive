package com.brvsk.ZenithActive.diet;

import org.springframework.stereotype.Component;

@Component
public class DietMapper {

    public DietResponse mapToResponse(Diet diet) {
        return DietResponse.builder()
                .dietId(diet.getId())
                .dietDayList(diet.getDailyMealPlans())
                .creationAt(diet.getCreatedAt())
                .build();
    }
}



