package com.brvsk.ZenithActive.loyalty;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoyaltyPointsServiceImpl implements LoyaltyPointsService{

    private final LoyaltyPointsRepository loyaltyPointsRepository;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;

    @Override
    public void addLoyaltyPoints(LoyaltyPointsCreateRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(request.getMemberId()));

        int totalLoyaltyPointsBefore = calculateTotalLoyaltyPoints(member);
        LoyaltyPoints loyaltyPoints = toEntity(request);
        loyaltyPoints.setMember(member);

        member.getLoyaltyPoints().add(loyaltyPoints);

        memberRepository.save(member);
        loyaltyPointsRepository.save(loyaltyPoints);

        checkPointsAndSendEmail(totalLoyaltyPointsBefore, request.getPointsAmount(), member.getEmail(), member.getFirstName());
    }

    @Override
    public int getTotalPointsForMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
        if (member.getLoyaltyPoints().equals(Set.of())) {
            return 0;
        }

        return loyaltyPointsRepository.sumPointsByMemberId(memberId);
    }

    @Override
    public long countGivenPointsToday() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        return loyaltyPointsRepository.countPointsInDateRange(startOfDay, endOfDay);
    }

    @Override
    public int calculateTotalLoyaltyPoints(Member member) {
        Set<LoyaltyPoints> loyaltyPoints = member.getLoyaltyPoints();
        int totalPoints = 0;

        if (loyaltyPoints != null) {
            for (LoyaltyPoints loyaltyPoint : loyaltyPoints) {
                if (loyaltyPoint != null) {
                    Integer pointsAmount = loyaltyPoint.getPointsAmount();
                    if (pointsAmount != null) {
                        totalPoints += pointsAmount;
                    }
                }
            }
        }

        return totalPoints;
    }

    private void checkPointsAndSendEmail(int currentPoints, int addedPoints, String memberEmail, String memberFirstName) {
        int totalPointsAfterAddition = currentPoints + addedPoints;

        if (currentPoints < 1000 && totalPointsAfterAddition >= 1000) {
            emailSender.sendLoyaltyPointsThresholdEmail(memberFirstName, memberEmail);
        }
    }

    private LoyaltyPoints toEntity(LoyaltyPointsCreateRequest request) {
        return LoyaltyPoints.builder()
                .loyaltyPointsType(request.getLoyaltyPointsType())
                .pointsAmount(request.getPointsAmount())
                .receivedAt(LocalDateTime.now())
                .build();
    }
}

