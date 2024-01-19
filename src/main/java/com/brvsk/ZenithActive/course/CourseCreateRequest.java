package com.brvsk.ZenithActive.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseCreateRequest {

    @NotNull(message = "Course type cannot be null")
    private CourseType courseType;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
