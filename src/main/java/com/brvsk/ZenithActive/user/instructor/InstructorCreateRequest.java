package com.brvsk.ZenithActive.user.instructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class InstructorCreateRequest {

    @NotNull
    private UUID userId;
    @NotBlank
    private String description;
    @NotNull
    private List<Speciality> specialities;

}
