package com.brvsk.ZenithActive.review;

import com.brvsk.ZenithActive.course.CourseNotFoundException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
