package com.brvsk.ZenithActive.diet.mealprefernce;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MemberMealPreferenceCreateRequest {
    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;

    @NotNull(message = "Meal Profile ID cannot be null")
    private UUID mealProfileId;

    @DecimalMin(value = "0.0", message = "Preference Score must be between 0.0 and 5.0")
    @DecimalMax(value = "5.0", message = "Preference Score must be between 0.0 and 5.0")
    private double preferenceScore;

}
