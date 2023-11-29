package com.brvsk.ZenithActive.review;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ReviewCreateRequest {
    @NotNull
    private UUID memberId;

    @NotNull
    private UUID courseId;
    @NotNull
    private Rating courseRating;
    @NotBlank
    private String courseComment;

    @NotNull
    private Rating instructorRating;
    @NotBlank
    private String instructorComment;

}
