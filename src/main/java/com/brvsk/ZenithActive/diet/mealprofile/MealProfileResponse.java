package com.brvsk.ZenithActive.diet.mealprofile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MealProfileResponse {
    private UUID id;
    private String name;
    private List<Long> ingredientIds;
    private MealCategory mealCategory;
    private List<Allergy> allergens;
    private double totalCalories;
    private double totalFat;
    private double totalProtein;
    private double totalCarbohydrates;
    private boolean isVegetarian;
    private boolean isVegan;
}
