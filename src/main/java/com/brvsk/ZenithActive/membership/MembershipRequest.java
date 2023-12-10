package com.brvsk.ZenithActive.membership;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class MembershipRequest {

    @NonNull
    private MembershipType membershipType;
    @NonNull
    private Integer numberOfMonths;
    @NonNull
    private UUID memberId;
}
