package com.brvsk.ZenithActive.membership;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService{

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;

    @Override
    public Membership addNewMembershipToMember(MembershipRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserNotFoundException(request.getMemberId()));

        Membership membership = toEntity(request);
        membership.setMember(member);
        membershipRepository.save(membership);

        member.setMembership(membership);
        memberRepository.save(member);


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
}
