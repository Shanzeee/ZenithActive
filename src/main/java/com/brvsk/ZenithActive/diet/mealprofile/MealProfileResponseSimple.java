package com.brvsk.ZenithActive.diet.mealprofile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MealProfileResponseSimple {
    private UUID id;
    private String name;
    List<String> ingredientsName;
    private double totalCalories;
}