package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.DietDay;
import com.brvsk.ZenithActive.diet.DietDayMeal;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.mealprofile.Allergy;
import com.brvsk.ZenithActive.diet.mealprofile.MealCategory;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DietGenerator {

    private final MealProfileRepository mealProfileRepository;
    private final DietRecommendationService dietRecommendationService;

    public DietDay generateDailyDietDay(DietRequest dietRequest) {
        double totalCaloricNeeds = CaloricNeedsCalculator.calculateTotalCaloricNeeds(
                dietRequest.getWeight(), dietRequest.getHeight(), dietRequest.getAge(),
                dietRequest.getGender(), dietRequest.getActivityLevel());

        List<MealProfile> filteredMealProfiles = filterMealProfiles(dietRequest, mealProfileRepository.findAll());

        Map<MealCategory, MealProfile> dietDayMeals = new HashMap<>();
        for (MealCategory category : MealCategory.values()) {
            double categoryCaloricNeeds = totalCaloricNeeds * getTargetPercentage(category) / 100.0;
            List<MealProfile> categoryMeals = selectMealsForCategory(filteredMealProfiles, category, categoryCaloricNeeds);
            MealProfile selectedMeal = dietRecommendationService.recommendMealForCategory(categoryMeals, dietRequest);
            dietDayMeals.put(category, selectedMeal);
        }

        return DietDay.builder()
                .dietDayMeals(createDietDayMeals(dietDayMeals))
                .build();
    }

    private List<DietDayMeal> createDietDayMeals(Map<MealCategory, MealProfile> dietDayMeals) {
        return dietDayMeals.entrySet().stream()
                .map(entry -> DietDayMeal.builder()
                        .mealCategory(entry.getKey())
                        .mealProfile(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MealProfile> filterMealProfiles(DietRequest dietRequest, List<MealProfile> mealProfiles) {
        List<MealProfile> filteredMeals = mealProfiles;

        if (dietRequest.isVegetarian()) {
            filteredMeals = filterVegetarianMeals(filteredMeals);
        }

        if (dietRequest.isVegan()) {
            filteredMeals = filterVeganMeals(filteredMeals);
        }

        filteredMeals = filterAllergenFreeMeals(filteredMeals, dietRequest.getAllergies());

        return filteredMeals;
    }

    private List<MealProfile> filterVegetarianMeals(List<MealProfile> mealProfiles) {
        return mealProfiles.stream()
                .filter(MealProfile::isVegetarian)
                .collect(Collectors.toList());
    }

    private List<MealProfile> filterVeganMeals(List<MealProfile> mealProfiles) {
        return mealProfiles.stream()
                .filter(MealProfile::isVegan)
                .collect(Collectors.toList());
    }

    private List<MealProfile> filterAllergenFreeMeals(List<MealProfile> mealProfiles, List<Allergy> allergies) {
        return mealProfiles.stream()
                .filter(mealProfile -> mealProfile.getAllergens().stream().noneMatch(allergies::contains))
                .collect(Collectors.toList());
    }

    private List<MealProfile> selectMealsForCategory(List<MealProfile> mealProfiles, MealCategory category, double categoryCaloricNeeds) {
        return mealProfiles.stream()
                .filter(meal -> meal.getMealCategory().equals(category))
                .sorted(Comparator.comparingDouble(meal -> Math.abs(meal.getTotalCalories() - categoryCaloricNeeds)))
                .limit(10)
                .collect(Collectors.toList());
    }

    private double getTargetPercentage(MealCategory category) {
        return switch (category) {
            case BREAKFAST -> 20.0;
            case LUNCH -> 30.0;
            case SNACK -> 10.0;
            case DINNER -> 25.0;
            case DESSERT -> 15.0;
        };
    }


}
