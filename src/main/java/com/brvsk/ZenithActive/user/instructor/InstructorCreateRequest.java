package com.brvsk.ZenithActive.user.instructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InstructorCreateRequest {

    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Specialities cannot be null")
    private List<Speciality> specialities;
}
