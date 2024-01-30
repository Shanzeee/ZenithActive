package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService{

    private final DietRepository dietRepository;
    private final DietGenerator dietGenerator;

    @Override
    public void createDiet(DietRequest dietRequest, int numberOfDays) {

    }

    @Override
    public void createOneDayDiet(DietRequest dietRequest) {
        DietDay dietDay = dietGenerator.generateDailyDietDay(dietRequest);

        Diet diet = Diet.builder()
                .member(dietRequest.getMember())
                .dailyMealPlans(Collections.singletonList(dietDay))
                .build();

        dietRepository.save(diet);
    }
}
