package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.CourseType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @Transactional
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> createNewSession(@RequestBody @Valid SessionCreateRequest request) {
        try {
            sessionService.createNewSession(request);
            return new ResponseEntity<>("Session created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        try {
            List<SessionResponse> sessions = sessionService.getAllSessions();
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byCourseType/{courseType}")
    public ResponseEntity<List<SessionResponse>> getSessionsForCourseType(@PathVariable CourseType courseType) {
        try {
            List<SessionResponse> sessions = sessionService.getSessionsForCourseType(courseType);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byInstructorId/{instructorId}")
    public ResponseEntity<List<SessionResponse>> getSessionsForInstructor(@PathVariable UUID instructorId) {
        try {
            List<SessionResponse> sessions = sessionService.getSessionsForInstructor(instructorId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
