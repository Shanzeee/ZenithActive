package com.brvsk.ZenithActive.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {
    void createNewReview(ReviewCreateRequest request);
    Page<ReviewResponse> getCourseReviews(UUID courseId, Pageable pageable);
    Page<ReviewResponse> getInstructorReviews(UUID instructorId, Pageable pageable);
    Double getAverageInstructorRating(UUID instructorId);
    Double getAverageCourseRating(UUID courseId);
}
