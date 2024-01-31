package com.brvsk.ZenithActive.diet.mealprefernce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberMealPreferenceRepository extends JpaRepository<MemberMealPreference, UUID> {

    default boolean userPrefersMeal(UUID memberId, UUID mealProfileId) {
        return findById(memberId).map(memberMealPreference ->
                        memberMealPreference.getMealProfile().getId().equals(mealProfileId)
                                && memberMealPreference.getPreferenceScore() > 3.5)
                .orElse(false);
    }
}
