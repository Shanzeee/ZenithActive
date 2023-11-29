package com.brvsk.ZenithActive.review;

import org.springframework.transaction.annotation.Transactional;

public interface ReviewService {
    @Transactional
    void createNewReview(ReviewCreateRequest request);
}
