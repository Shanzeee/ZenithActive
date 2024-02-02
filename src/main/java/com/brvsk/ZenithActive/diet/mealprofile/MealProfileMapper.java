package com.brvsk.ZenithActive.diet.mealprofile;

import com.brvsk.ZenithActive.diet.ingredient.Ingredient;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MealProfileMapper {

    public MealProfileResponse mapToResponse(MealProfile mealProfile) {
        return MealProfileResponse.builder()
                .id(mealProfile.getId())
                .name(mealProfile.getName())
                .ingredientsName(mealProfile.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()))
                .mealCategory(mealProfile.getMealCategory())
                .allergens(mealProfile.getAllergens())
                .totalCalories(mealProfile.getTotalCalories())
                .totalFat(mealProfile.getTotalFat())
                .totalProtein(mealProfile.getTotalProtein())
                .totalCarbohydrates(mealProfile.getTotalCarbohydrates())
                .isVegetarian(mealProfile.isVegetarian())
                .isVegan(mealProfile.isVegan())
                .build();
    }

    public MealProfileResponseSimple mapToResponseSimple(MealProfile mealProfile) {
        return MealProfileResponseSimple.builder()
                .id(mealProfile.getId())
                .name(mealProfile.getName())
                .ingredientsName(mealProfile.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()))
                .totalCalories(mealProfile.getTotalCalories())
                .build();
    }

    public void mapToEntity(MealProfileCreateRequest request, MealProfile existingMealProfile) {
        existingMealProfile.setName(request.getName());
        existingMealProfile.setMealCategory(request.getMealCategory());
        existingMealProfile.setAllergens(request.getAllergens());
        existingMealProfile.setTotalCalories(request.getTotalCalories());
        existingMealProfile.setTotalFat(request.getTotalFat());
        existingMealProfile.setTotalProtein(request.getTotalProtein());
        existingMealProfile.setTotalCarbohydrates(request.getTotalCarbohydrates());
        existingMealProfile.setVegetarian(request.isVegetarian());
        existingMealProfile.setVegan(request.isVegan());
    }

}
