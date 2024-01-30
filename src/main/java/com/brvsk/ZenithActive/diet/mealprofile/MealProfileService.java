package com.brvsk.ZenithActive.diet.mealprofile;

import java.util.List;
import java.util.UUID;

public interface MealProfileService {
    List<MealProfileResponse> getAllMealProfiles();
    MealProfileResponse getMealProfileById(UUID id);
    MealProfileResponse createMealProfile(MealProfileCreateRequest createRequest);
    MealProfileResponse updateMealProfile(UUID id, MealProfileCreateRequest updateRequest);
    void deleteMealProfile(UUID id);
}
