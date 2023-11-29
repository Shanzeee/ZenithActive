package com.brvsk.ZenithActive.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReviewResponse {
    private UUID reviewId;
    private UUID reviewedByUserId;
    private String userName;
    private Rating rating;
    private String comment;
    private LocalDate uploadAt;
}


