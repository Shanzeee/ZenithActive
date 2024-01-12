package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollMemberToCourse(@RequestParam UUID courseId, @RequestParam UUID userId) {
        try {
            courseService.enrolMemberToCourse(courseId, userId);
            return ResponseEntity.ok("Member enrolled to course successfully");
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
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

    @GetMapping("/byInstructorId/{instructorId}")
    public ResponseEntity<List<CourseResponse>> getCoursesForInstructor(@PathVariable UUID instructorId) {
        try {
            List<CourseResponse> courses = courseService.getCoursesForInstructor(instructorId);
            return ResponseEntity.ok(courses);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{courseId}/members")
    public ResponseEntity<Set<MemberResponse>> getMembersForCourse(@PathVariable UUID courseId) {
        try {
            Set<MemberResponse> members = courseService.getMembersForCourse(courseId);
            return ResponseEntity.ok(members);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<Set<CourseResponse>> getCoursesForMember(@PathVariable UUID userId) {
        try {
            Set<CourseResponse> courses = courseService.getCoursesForMember(userId);
            return ResponseEntity.ok(courses);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
