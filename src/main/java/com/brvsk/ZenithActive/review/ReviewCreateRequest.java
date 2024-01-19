package com.brvsk.ZenithActive.review;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ReviewCreateRequest {

    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;
    @NotNull(message = "Reviewed entity ID cannot be null")
    private UUID reviewedEntityId;
    @NotNull(message = "Rating cannot be null")
    private Rating rating;
    @Size(max = 500, message = "Comment cannot be more than 500 characters")
    private String comment;
}
