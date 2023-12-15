package com.brvsk.ZenithActive.membership;

import com.brvsk.ZenithActive.loyalty.LoyaltyPointsCreateRequest;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsType;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService{

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final LoyaltyPointsService loyaltyPointsService;

    @Override
    public Membership addNewMembershipToMember(MembershipRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(request.getMemberId()));

        Membership membership = toEntity(request);
        membership.setMember(member);
        membershipRepository.save(membership);

        member.setMembership(membership);
        memberRepository.save(member);

        LoyaltyPointsCreateRequest loyaltyPointsCreateRequest = buildLoyaltyPointsCreateRequest(request.getMembershipType(), request.getNumberOfMonths(), request.getMemberId());
        loyaltyPointsService.addLoyaltyPoints(loyaltyPointsCreateRequest);

        return membership;
    }


    private Membership toEntity (MembershipRequest request) {
        return  Membership.builder()
                .membershipType(request.getMembershipType())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusMonths(request.getNumberOfMonths())
                        .plusDays(1))
                .build();
    }

    private LoyaltyPointsCreateRequest buildLoyaltyPointsCreateRequest(MembershipType membershipType, int months, UUID memberId){
        return LoyaltyPointsCreateRequest
                .builder()
                .loyaltyPointsType(LoyaltyPointsType.MEMBERSHIP)
                .pointsAmount(calculateLoyaltyPoints(membershipType, months))
                .memberId(memberId)
                .build();
    }

    private int calculateLoyaltyPoints(MembershipType membershipType, int months) {
        int basePoints = 100;

        return switch (membershipType) {
            case FULL -> basePoints * months * 3;
            case GYM, POOL -> basePoints * months;
            case GYM_PLUS, POOL_PLUS -> (int) (basePoints * months * 1.2);
            case PREMIUM -> basePoints * months * 4;
            case PREMIUM_PLUS -> basePoints * months * 5;
        };
    }
}
