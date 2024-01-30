package com.brvsk.ZenithActive.diet.ingredient;

import com.brvsk.ZenithActive.diet.mealprofile.MealProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Table(name = "ingredient")
@Entity(name = "Ingredient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_seq")
    @SequenceGenerator(name = "ingredient_seq", sequenceName = "ingredient_seq", allocationSize = 1)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private List<MealProfile> mealProfiles;
}
