package com.brvsk.ZenithActive.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CourseCreateRequest {
    @NotNull
    private CourseType courseType;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Integer groupSize;
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private UUID facilityId;
    @NotNull
    private UUID instructorId;
}
