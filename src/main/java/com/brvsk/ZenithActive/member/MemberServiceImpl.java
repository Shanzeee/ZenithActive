package com.brvsk.ZenithActive.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void createMember(MemberCreateRequest request){
        Member member = toEntity(request);

        memberRepository.save(member);
    }

    private Member toEntity(MemberCreateRequest request){
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setHeight(request.getHeight());
        member.setWeight(request.getWeight());
        return member;
    }
}
