package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.DietDay;
import com.brvsk.ZenithActive.diet.DietDayMeal;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.mealprofile.MealCategory;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DietGenerator {

    private final MealProfileService mealProfileService;
    private final DietRecommendationService dietRecommendationService;


    public List<DietDay> generateDiet(DietRequest dietRequest) {
        double totalCaloricNeeds = calculateTotalCaloricNeeds(dietRequest);
        List<DietDay> weeklyDiet = new ArrayList<>();
        Set<UUID> usedMealIds = new HashSet<>();

        for (int day = 0; day < dietRequest.getDays(); day++) {
            DietDay dietDay = generateDailyDietDay(dietRequest, totalCaloricNeeds, usedMealIds);
            weeklyDiet.add(dietDay);
            dietDay.getDietDayMeals().forEach(meal -> usedMealIds.add(meal.getMealProfile().getId()));
        }

        return weeklyDiet;
    }

    private DietDay generateDailyDietDay(DietRequest dietRequest, double totalCaloricNeeds, Set<UUID> usedMealIds) {


        List<MealProfile> filteredMealProfiles = mealProfileService.filterMeals(dietRequest).stream()
                .filter(meal -> !usedMealIds.contains(meal.getId()))
                .collect(Collectors.toList());

        Map<MealCategory, MealProfile> dietDayMeals = new HashMap<>();

        for (MealCategory category : MealCategory.values()) {
            double categoryCaloricNeeds = totalCaloricNeeds * getTargetPercentage(category) / 100.0;
            List<MealProfile> categoryMeals = selectMealsForCategory(filteredMealProfiles, category, categoryCaloricNeeds);
            Optional<MealProfile> selectedMeal = dietRecommendationService.recommendMealForCategory(categoryMeals, dietRequest);
            MealProfile finalMeal = selectedMeal.orElseGet(() -> findClosestCaloricMeal(categoryMeals, categoryCaloricNeeds));
            dietDayMeals.put(category, finalMeal);
        }

        return DietDay.builder()
                .dietDayMeals(createDietDayMeals(dietDayMeals))
                .build();
    }

    private double calculateTotalCaloricNeeds(DietRequest dietRequest) {
        return CaloricNeedsCalculator.calculateTotalCaloricNeeds(dietRequest);
    }

    private List<DietDayMeal> createDietDayMeals(Map<MealCategory, MealProfile> dietDayMeals) {
        return dietDayMeals.entrySet().stream()
                .map(entry -> DietDayMeal.builder()
                        .mealCategory(entry.getKey())
                        .mealProfile(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MealProfile> selectMealsForCategory(List<MealProfile> mealProfiles, MealCategory category, double categoryCaloricNeeds) {
        return mealProfiles.stream()
                .filter(meal -> meal.getMealCategory().equals(category))
                .sorted(Comparator.comparingDouble(meal -> Math.abs(meal.getTotalCalories() - categoryCaloricNeeds)))
                .limit(10)
                .collect(Collectors.toList());
    }

    private MealProfile findClosestCaloricMeal(List<MealProfile> meals, double targetCalories) {
        return meals.stream()
                .min(Comparator.comparingDouble(meal -> Math.abs(meal.getTotalCalories() - targetCalories)))
                .orElseThrow(() -> new RuntimeException("Cannot find default meal: " + targetCalories));
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
