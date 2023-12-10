package com.brvsk.ZenithActive.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ReviewCreateRequest {

    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;

    @NotNull(message = "Course ID cannot be null")
    private UUID courseId;

    @NotNull(message = "Course rating cannot be null")
    private Rating courseRating;

    @Min(value = 10, message = "Course comment must be at least 10 characters")
    @Max(value = 500, message = "Course comment cannot be more than 500 characters")
    private String courseComment;

    @NotNull(message = "Instructor rating cannot be null")
    private Rating instructorRating;

    @Min(value = 10, message = "Instructor comment must be at least 10 characters")
    @Max(value = 500, message = "Instructor comment cannot be more than 500 characters")
    private String instructorComment;
}
