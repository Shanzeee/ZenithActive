package com.brvsk.ZenithActive.member;

import com.brvsk.ZenithActive.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberCreateRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;

    private Integer height;
    private Integer weight;
}
