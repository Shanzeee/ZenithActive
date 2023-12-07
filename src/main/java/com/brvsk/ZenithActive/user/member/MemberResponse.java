package com.brvsk.ZenithActive.user.member;

import com.brvsk.ZenithActive.user.Gender;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private Gender gender;
}
