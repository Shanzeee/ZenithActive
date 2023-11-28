package com.brvsk.ZenithActive.course;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<String> createNewCourse(@RequestBody @Valid CourseCreateRequest request) {
        try {
            courseService.createNewCourse(request);
            return new ResponseEntity<>("Course created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/byCourseType/{courseType}")
    public List<CourseResponse> getCoursesForCourseType(@PathVariable CourseType courseType) {
        return courseService.getCoursesForCourseType(courseType);
    }

    @GetMapping("/byInstructorId/{instructorId}")
    public List<CourseResponse> getCoursesForInstructor(@PathVariable UUID instructorId) {
        return courseService.getCoursesForInstructor(instructorId);
    }
}
