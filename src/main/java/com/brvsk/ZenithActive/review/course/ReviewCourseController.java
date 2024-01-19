package com.brvsk.ZenithActive.review.course;

import com.brvsk.ZenithActive.course.CourseNotFoundException;
import com.brvsk.ZenithActive.review.ReviewCreateRequest;
import com.brvsk.ZenithActive.review.ReviewResponse;
import com.brvsk.ZenithActive.review.ReviewService;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews/courses")
@RequiredArgsConstructor
public class ReviewCourseController {

    private final ReviewService reviewCourseServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid ReviewCreateRequest request) {
        try {
            reviewCourseServiceImpl.createNewReview(request);
            return new ResponseEntity<>("Review created successfully for course", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the review for course", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Page<ReviewResponse>> getCourseReviews(@PathVariable UUID courseId, Pageable pageable) {
        Page<ReviewResponse> reviews = reviewCourseServiceImpl.getReviews(courseId, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/average/{courseId}")
    public ResponseEntity<Double> getAverageCourseRating(@PathVariable UUID courseId) {
        try {
            Double averageRating = reviewCourseServiceImpl.getAverageRating(courseId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
