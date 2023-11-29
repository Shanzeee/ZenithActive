package com.brvsk.ZenithActive.review;

import com.brvsk.ZenithActive.course.CourseNotFoundException;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid ReviewCreateRequest request) {
        try {
            reviewService.createNewReview(request);
            return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
        } catch (UserNotFoundException | CourseNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<ReviewResponse>> getCourseReviews(@PathVariable UUID courseId, Pageable pageable) {
        Page<ReviewResponse> reviews = reviewService.getCourseReviews(courseId, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<Page<ReviewResponse>> getInstructorReviews(@PathVariable UUID instructorId, Pageable pageable) {
        Page<ReviewResponse> reviews = reviewService.getInstructorReviews(instructorId, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/course/average/{courseId}")
    public ResponseEntity<Double> getAverageCourseRating(@PathVariable UUID courseId) {
        try {
            Double averageRating = reviewService.getAverageCourseRating(courseId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/instructor/average/{instructorId}")
    public ResponseEntity<Double> getAverageInstructorRating(@PathVariable UUID instructorId) {
        try {
            Double averageRating = reviewService.getAverageInstructorRating(instructorId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
