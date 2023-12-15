package com.brvsk.ZenithActive.loyalty;

import java.util.UUID;

public interface LoyaltyPointsService {
    void addLoyaltyPoints(LoyaltyPointsCreateRequest request);

    int getTotalPointsForMember(UUID memberId);

    long countGivenPointsToday();
}
