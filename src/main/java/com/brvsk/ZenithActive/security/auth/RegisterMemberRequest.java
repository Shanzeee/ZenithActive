package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberRequest {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private Gender gender;
  private Integer height;
  private Integer weight;
}
