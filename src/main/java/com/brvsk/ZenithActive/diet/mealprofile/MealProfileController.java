package com.brvsk.ZenithActive.diet.mealprofile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meal-profiles")
@RequiredArgsConstructor
public class MealProfileController {

    private final MealProfileService mealProfileService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> createNewMealProfile(@RequestBody MealProfileCreateRequest request) {
        try {
            mealProfileService.createMealProfile(request);
            return new ResponseEntity<>("MealProfile created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<MealProfileResponse>> getAllMealProfiles() {
        try {
            List<MealProfileResponse> mealProfiles = mealProfileService.getAllMealProfiles();
            return ResponseEntity.ok(mealProfiles);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealProfileResponse> getMealProfileById(@PathVariable UUID id) {
        try {
            MealProfileResponse mealProfile = mealProfileService.getMealProfileById(id);
            return ResponseEntity.ok(mealProfile);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateMealProfile(@PathVariable UUID id, @RequestBody MealProfileCreateRequest updateRequest) {
        try {
            MealProfileResponse updatedMealProfile = mealProfileService.updateMealProfile(id, updateRequest);
            return ResponseEntity.ok(updatedMealProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> deleteMealProfile(@PathVariable UUID id) {
        try {
            mealProfileService.deleteMealProfile(id);
            return new ResponseEntity<>("MealProfile deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

