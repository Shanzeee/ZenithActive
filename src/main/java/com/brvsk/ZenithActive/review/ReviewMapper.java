package com.brvsk.ZenithActive.review;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewCourseResponse(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .reviewedByUserId(review.getMember().getUserId())
                .userName(review.getMember().getFirstName() + " " + review.getMember().getLastName())
                .rating(review.getCourseRating())
                .comment(review.getCourseComment())
                .uploadAt(review.getTimestamp())
                .build();
    }

    public ReviewResponse toReviewInstructorResponse(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .reviewedByUserId(review.getMember().getUserId())
                .userName(review.getMember().getFirstName() + " " + review.getMember().getLastName())
                .rating(review.getInstructorRating())
                .comment(review.getInstructorComment())
                .uploadAt(review.getTimestamp())
                .build();
    }
}
