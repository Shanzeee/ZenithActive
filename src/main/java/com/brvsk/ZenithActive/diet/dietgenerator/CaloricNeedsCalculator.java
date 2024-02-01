package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.dietprofile.ActivityLevel;
import com.brvsk.ZenithActive.diet.dietprofile.DietGoal;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.user.Gender;
import org.springframework.stereotype.Component;

@Component
public class CaloricNeedsCalculator {
    public static double calculateTotalCaloricNeeds(DietRequest dietRequest) {
        double bmr = calculateBMR(dietRequest.getWeight(), dietRequest.getHeight(), dietRequest.getAge(), dietRequest.getGender());
        double totalCaloricNeeds = bmr * getActivityLevelPalFactor(dietRequest.getActivityLevel());
        return adjustCaloricNeedsByDietGoal(totalCaloricNeeds, dietRequest.getDietGoal());
    }

    private static double calculateBMR(double weight, double height, int age, Gender gender) {
        return switch (gender) {
            case FEMALE -> 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
            case MALE -> 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
            case OTHER -> 340 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        };
    }

    private static double getActivityLevelPalFactor(ActivityLevel activityLevel) {
        return switch (activityLevel) {
            case SEDENTARY -> 1.2;
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            case EXTRA_ACTIVE -> 1.9;
        };
    }

    private static double adjustCaloricNeedsByDietGoal(double totalCalories, DietGoal dietGoal) {
        return switch (dietGoal) {
            case WEIGHT_LOSS -> totalCalories - 500;
            case MUSCLE_GAIN -> totalCalories + 500;
            case MAINTENANCE -> totalCalories;
            case GENERAL_HEALTH -> totalCalories + 100;
            case SPORTS_PERFORMANCE -> totalCalories + 300;
        };
    }
}
