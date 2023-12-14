package com.brvsk.ZenithActive.loyalty;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyPointsServiceImpl implements LoyaltyPointsService{

    private final LoyaltyPointsRepository loyaltyPointsRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addLoyaltyPoints(LoyaltyPointsCreateRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(request.getMemberId()));

        LoyaltyPoints loyaltyPoints = toEntity(request);

        member.getLoyaltyPoints().add(loyaltyPoints);

        memberRepository.save(member);
        loyaltyPointsRepository.save(loyaltyPoints);
    }

    private LoyaltyPoints toEntity(LoyaltyPointsCreateRequest request) {
        return LoyaltyPoints.builder()
                .loyaltyPointsType(request.getLoyaltyPointsType())
                .pointsAmount(request.getPointsAmount())
                .receivedAt(LocalDateTime.now())
                .build();
    }
}

