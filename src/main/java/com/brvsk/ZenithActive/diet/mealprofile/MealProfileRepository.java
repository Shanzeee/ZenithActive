package com.brvsk.ZenithActive.diet.mealprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealProfileRepository extends JpaRepository<MealProfile, UUID> {
}
