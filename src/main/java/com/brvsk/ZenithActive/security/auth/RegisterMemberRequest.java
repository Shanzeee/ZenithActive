package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.utils.validation.ValidEmail;
import com.brvsk.ZenithActive.utils.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMemberRequest {

  @NotBlank(message = "First name cannot be blank")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  private String lastName;

  @ValidEmail
  private String email;

  @ValidPassword
  private String password;

  @NotNull(message = "Gender cannot be null")
  private Gender gender;

  private Integer height;

  private Integer weight;

  private boolean isEmailNewsletter;
}
