package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewInstructorFromEmployee(@RequestBody @Valid InstructorCreateRequest request) {
        try {
            instructorService.createNewInstructorFromEmployee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Instructor created successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with provided ID.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{instructorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteInstructor(@PathVariable UUID instructorId) {
        try {
            instructorService.deleteInstructor(instructorId);
            return ResponseEntity.ok("Instructor deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with provided ID.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
