package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.Gender;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMemberRequest {

  @NotBlank(message = "First name cannot be blank")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  private String lastName;

  @Email(message = "Invalid email address")
  @NotBlank(message = "Email cannot be blank")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 6, message = "Password must be at least 6 characters long")
  private String password;

  @NotNull(message = "Gender cannot be null")
  private Gender gender;

  private Integer height;

  private Integer weight;

  private boolean isEmailNewsletter;
}
