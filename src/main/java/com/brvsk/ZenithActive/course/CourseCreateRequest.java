package com.brvsk.ZenithActive.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CourseCreateRequest {

    @NotNull(message = "Course type cannot be null")
    private CourseType courseType;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Group size cannot be null")
    @Min(value = 0, message = "Group size cannot be negative")
    @Max(value = 100, message = "Group size cannot be greater than 100")
    private Integer groupSize;

    @NotNull(message = "date cannot be null")
    private LocalDate localDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Facility ID cannot be null")
    private UUID facilityId;

    @NotNull(message = "Instructor ID cannot be null")
    private UUID instructorId;
}
