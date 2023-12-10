package com.brvsk.ZenithActive.membership;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
public class MembershipRequest {

    @NonNull
    private MembershipType membershipType;

    @NotNull(message = "Number of months cannot be null")
    @Min(value = 1, message = "Number of months must be at least 1")
    private Integer numberOfMonths;

    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;
}
