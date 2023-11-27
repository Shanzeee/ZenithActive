package com.brvsk.ZenithActive.instructor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping("/create")
    public ResponseEntity<String> createNewInstructor(@RequestBody @Valid InstructorCreateRequest request) {
        instructorService.createNewInstructor(request);
        return new ResponseEntity<>("Instructor created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{instructorId}")
    public ResponseEntity<String> deleteInstructor(@PathVariable UUID instructorId) {
        instructorService.deleteInstructor(instructorId);
        return new ResponseEntity<>("Instructor deleted successfully", HttpStatus.OK);
    }
}
