package com.brvsk.ZenithActive.diet.ingredient;

import com.brvsk.ZenithActive.diet.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
