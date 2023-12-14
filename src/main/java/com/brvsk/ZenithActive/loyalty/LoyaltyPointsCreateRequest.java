package com.brvsk.ZenithActive.loyalty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class LoyaltyPointsCreateRequest {

    @NonNull
    private LoyaltyPointsType loyaltyPointsType;
    @NotNull(message = "Number of months cannot be null")
    @Min(value = 1, message = "Number of months must be at least 1")
    private Integer pointsAmount;
    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;
}
