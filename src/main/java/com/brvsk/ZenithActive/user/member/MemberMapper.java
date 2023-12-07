package com.brvsk.ZenithActive.user.member;

import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberResponse toMemberResponse(Member member){
        return MemberResponse.builder()
                .userId(member.getUserId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .gender(member.getGender())
                .build();
    }
}
