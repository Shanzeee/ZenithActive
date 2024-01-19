package com.brvsk.ZenithActive.course.session;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class SessionCreateRequest {

    @NotNull(message = "Course ID cannot be null")
    private UUID courseId;

    @NotNull(message = "Facility ID cannot be null")
    private UUID facilityId;

    @NotNull(message = "Instructor ID cannot be null")
    private UUID instructorId;

    @NotNull(message = "Group size cannot be null")
    @Min(value = 0, message = "Group size cannot be negative")
    @Max(value = 100, message = "Group size cannot be greater than 100")
    private Integer groupSize;

    @NotNull(message = "Local date cannot be null")
    @FutureOrPresent(message = "Local date must be in the present or future")
    private LocalDate localDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

}
