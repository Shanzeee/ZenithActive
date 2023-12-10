package com.brvsk.ZenithActive.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Email(message = "Invalid email format")
    private String email;
}