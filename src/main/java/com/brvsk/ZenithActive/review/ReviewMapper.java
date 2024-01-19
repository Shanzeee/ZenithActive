package com.brvsk.ZenithActive.review;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .reviewedByUserId(review.getMember().getUserId())
                .userName(review.getMember().getFirstName() + " " + review.getMember().getLastName())
                .rating(review.getRating())
                .comment(review.getComment())
                .uploadAt(review.getTimestamp())
                .build();
    }
}
