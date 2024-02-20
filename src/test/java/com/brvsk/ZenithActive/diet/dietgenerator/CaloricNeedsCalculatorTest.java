package com.brvsk.ZenithActive.diet.dietgenerator;

import com.brvsk.ZenithActive.diet.dietprofile.ActivityLevel;
import com.brvsk.ZenithActive.diet.dietprofile.DietGoal;
import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.user.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CaloricNeedsCalculatorTest {

    @Test
    public void testCalculateTotalCaloricNeedsForMaleWeightLoss() {
        // Given
        DietRequest dietRequest = new DietRequest();
        dietRequest.setWeight(70);
        dietRequest.setHeight(175);
        dietRequest.setAge(25);
        dietRequest.setGender(Gender.MALE);
        dietRequest.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        dietRequest.setDietGoal(DietGoal.WEIGHT_LOSS);

        // When
        double totalCaloricNeeds = CaloricNeedsCalculator.calculateTotalCaloricNeeds(dietRequest);

        // Then
        double expectedBMR = 66 + (13.7 * 70) + (5 * 175) - (6.8 * 25);
        double expectedTotalNeeds = (expectedBMR * 1.55) - 500;
        assertEquals(expectedTotalNeeds, totalCaloricNeeds, 0.1);
    }

    @Test
    public void testCalculateTotalCaloricNeedsForFemaleMuscleGain() {
        // Given
        DietRequest dietRequest = new DietRequest();
        dietRequest.setWeight(60);
        dietRequest.setHeight(165);
        dietRequest.setAge(30);
        dietRequest.setGender(Gender.FEMALE);
        dietRequest.setActivityLevel(ActivityLevel.LIGHTLY_ACTIVE);
        dietRequest.setDietGoal(DietGoal.MUSCLE_GAIN);

        // When
        double totalCaloricNeeds = CaloricNeedsCalculator.calculateTotalCaloricNeeds(dietRequest);

        // Then
        double expectedBMR = 655 + (9.6 * 60) + (1.8 * 165) - (4.7 * 30);
        double expectedTotalNeeds = (expectedBMR * 1.375) + 500;
        assertEquals(expectedTotalNeeds, totalCaloricNeeds, 0.1);
    }

    @Test
    public void testCalculateTotalCaloricNeedsForOtherGeneralHealth() {
        // Given
        DietRequest dietRequest = new DietRequest();
        dietRequest.setWeight(80);
        dietRequest.setHeight(180);
        dietRequest.setAge(40);
        dietRequest.setGender(Gender.OTHER);
        dietRequest.setActivityLevel(ActivityLevel.SEDENTARY);
        dietRequest.setDietGoal(DietGoal.GENERAL_HEALTH);

        // When
        double totalCaloricNeeds = CaloricNeedsCalculator.calculateTotalCaloricNeeds(dietRequest);

        // Then
        double expectedBMR = 340 + (9.6 * 80) + (1.8 * 180) - (4.7 * 40);
        double expectedTotalNeeds = (expectedBMR * 1.2) + 100;
        assertEquals(expectedTotalNeeds, totalCaloricNeeds, 0.1);
    }
}