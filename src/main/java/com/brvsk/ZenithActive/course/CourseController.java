package com.brvsk.ZenithActive.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> createNewCourse(@RequestBody CourseCreateRequest request) {
        try {
            courseService.createNewCourse(request);
            return new ResponseEntity<>("Course created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        try {
            List<CourseResponse> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byCourseType/{courseType}")
    public ResponseEntity<List<CourseResponse>> getCoursesForCourseType(@PathVariable CourseType courseType) {
        try {
            List<CourseResponse> courses = courseService.getCoursesForCourseType(courseType);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
