package com.brvsk.ZenithActive.diet;

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

    public DietDay generateDailyDietDay(DietRequest dietRequest) {
        double totalCaloricNeeds = CaloricNeedsCalculator.calculateTotalCaloricNeeds(
                dietRequest.getWeight(), dietRequest.getHeight(), dietRequest.getAge(),
                dietRequest.getGender(), dietRequest.getActivityLevel());

        List<MealProfile> availableMealProfiles = mealProfileRepository.findAll();

        List<MealProfile> filteredMealProfiles = filterMealProfiles(dietRequest, availableMealProfiles);

        Map<MealCategory, MealProfile> dietDayMeals = new HashMap<>();

        dietDayMeals.put(MealCategory.BREAKFAST, selectMeal(filteredMealProfiles, MealCategory.BREAKFAST, totalCaloricNeeds));
        dietDayMeals.put(MealCategory.LUNCH, selectMeal(filteredMealProfiles, MealCategory.LUNCH, totalCaloricNeeds));
        dietDayMeals.put(MealCategory.DINNER, selectMeal(filteredMealProfiles, MealCategory.DINNER, totalCaloricNeeds));
        dietDayMeals.put(MealCategory.DESSERT, selectMeal(filteredMealProfiles, MealCategory.DESSERT, totalCaloricNeeds));

        double remainingCalories = calculateRemainingCalories(new ArrayList<>(dietDayMeals.values()), totalCaloricNeeds);
        dietDayMeals.put(MealCategory.SNACK, selectSnacks(filteredMealProfiles, remainingCalories));

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

    private MealProfile selectMeal(List<MealProfile> mealProfiles, MealCategory category, double totalCaloricNeeds) {
        List<MealProfile> eligibleMeals = mealProfiles.stream()
                .filter(mealProfile -> mealProfile.getMealCategory().equals(category))
                .sorted(Comparator.comparingDouble(mealProfile -> calculatePercentageDifference(mealProfile, category, totalCaloricNeeds)))
                .toList();

        return eligibleMeals.get(0);
    }

    private double calculatePercentageDifference(MealProfile mealProfile, MealCategory category, double totalCaloricNeeds) {
        double targetPercentage = getTargetPercentage(category);
        double mealCalories = mealProfile.getTotalCalories();
        double currentPercentage = (mealCalories / totalCaloricNeeds) * 100.0;
        return Math.abs(targetPercentage - currentPercentage);
    }

    private double calculateRemainingCalories(List<MealProfile> dietPlan, double totalCaloricNeeds) {
        double caloriesConsumed = dietPlan.stream()
                .mapToDouble(MealProfile::getTotalCalories)
                .sum();
        return Math.max(0, totalCaloricNeeds - caloriesConsumed);
    }

    private MealProfile selectSnacks(List<MealProfile> mealProfiles, double remainingCalories) {
        List<MealProfile> eligibleSnacks = mealProfiles.stream()
                .filter(mealProfile -> mealProfile.getMealCategory().equals(MealCategory.SNACK))
                .filter(snack -> snack.getTotalCalories() >= remainingCalories)
                .sorted(Comparator.comparingDouble(MealProfile::getTotalCalories))
                .toList();

        return eligibleSnacks.get(0);
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
