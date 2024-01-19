package com.brvsk.ZenithActive.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {
    void createNewReview(ReviewCreateRequest request);
    Page<ReviewResponse> getReviews(UUID instructorId, Pageable pageable);
    Double getAverageRating(UUID instructorId);
}
