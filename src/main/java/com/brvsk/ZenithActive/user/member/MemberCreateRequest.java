package com.brvsk.ZenithActive.user.member;

import com.brvsk.ZenithActive.user.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Height must be a positive integer")
    private Integer height;

    @Positive(message = "Weight must be a positive integer")
    private Integer weight;

    private boolean emailNewsletter;
}
