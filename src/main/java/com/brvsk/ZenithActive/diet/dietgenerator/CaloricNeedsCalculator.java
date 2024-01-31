package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.dietprofile.ActivityLevel;
import com.brvsk.ZenithActive.user.Gender;
import org.springframework.stereotype.Component;

@Component
public class CaloricNeedsCalculator {
    public static double calculateTotalCaloricNeeds(double weight, double height, int age, Gender gender, ActivityLevel activityLevel) {
        double bmr = calculateBMR(weight, height, age, gender);
        return bmr * getActivityLevelPalFactor(activityLevel);
    }

    private static double calculateBMR(double weight, double height, int age, Gender gender) {
        return switch (gender) {
            case FEMALE -> 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
            case MALE -> 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
            case OTHER -> 0.0;
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
}
