package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.mealprofile.MealCategory;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "diet_day_meal")
@Entity(name = "DietDayMeal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietDayMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MealCategory mealCategory;

    @ManyToOne
    @JoinColumn(name = "meal_profile_id")
    private MealProfile mealProfile;

}
