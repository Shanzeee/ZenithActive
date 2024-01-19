package com.brvsk.ZenithActive.review.instructor;

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
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews/instructors")
public class ReviewInstructorController {

    private final ReviewService reviewInstructorServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid ReviewCreateRequest request) {
        try {
            reviewInstructorServiceImpl.createNewReview(request);
            return new ResponseEntity<>("Review created successfully for instructor", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the review for instructor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<Page<ReviewResponse>> getInstructorReviews(@PathVariable UUID instructorId, Pageable pageable) {
        Page<ReviewResponse> reviews = reviewInstructorServiceImpl.getReviews(instructorId, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/average/{instructorId}")
    public ResponseEntity<Double> getAverageInstructorRating(@PathVariable UUID instructorId) {
        try {
            Double averageRating = reviewInstructorServiceImpl.getAverageRating(instructorId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
