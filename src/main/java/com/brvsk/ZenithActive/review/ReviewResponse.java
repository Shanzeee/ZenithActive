package com.brvsk.ZenithActive.review;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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


