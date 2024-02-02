package com.brvsk.ZenithActive.diet.mealprofile;

import com.brvsk.ZenithActive.diet.ingredient.Ingredient;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "meal_profile")
@Entity(name = "MealProfile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class MealProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_profile_seq")
    @SequenceGenerator(name = "meal_profile_seq", sequenceName = "meal_profile_seq", allocationSize = 1)
    private UUID id;

    private String name;


    @ManyToMany
    @JoinTable(
            name = "meal_profile_ingredients",
            joinColumns = @JoinColumn(name = "meal_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @Enumerated(EnumType.STRING)
    private MealCategory mealCategory;

    @ElementCollection(targetClass = Allergy.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "meal_profile_allergens", joinColumns = @JoinColumn(name = "meal_profile_id"))
    @Column(name = "allergen")
    private List<Allergy> allergens;

    private double totalCalories;
    private double totalFat;
    private double totalProtein;
    private double totalCarbohydrates;

    private boolean isVegetarian;
    private boolean isVegan;

}
