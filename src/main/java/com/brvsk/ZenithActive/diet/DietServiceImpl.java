package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietgenerator.DietGenerator;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService{

    private final DietRepository dietRepository;
    private final DietGenerator dietGenerator;

    @Override
    public void createDiet(DietRequest dietRequest) {
        List<DietDay> dietDayList = dietGenerator.generateDiet(dietRequest);

        Diet diet = Diet
                .builder()
                .dailyMealPlans(dietDayList)
                .build();

        dietRepository.save(diet);
    }
}
