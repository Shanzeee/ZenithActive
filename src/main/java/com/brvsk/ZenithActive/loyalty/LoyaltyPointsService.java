package com.brvsk.ZenithActive.loyalty;

import com.brvsk.ZenithActive.user.member.Member;

import java.util.UUID;

public interface LoyaltyPointsService {
    void addLoyaltyPoints(LoyaltyPointsCreateRequest request);
    int getTotalPointsForMember(UUID memberId);
    long countGivenPointsToday();
    int calculateTotalLoyaltyPoints(Member member);
}
