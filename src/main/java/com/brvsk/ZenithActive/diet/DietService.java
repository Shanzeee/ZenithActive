package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileResponseSimple;

import java.util.List;
import java.util.UUID;

public interface DietService {
    void createDiet(DietRequest dietRequest);

    List<DietResponse> getDietsForMember(UUID memberId);

    List<MealProfileResponseSimple> getMealsForDiets(UUID memberId);
}
