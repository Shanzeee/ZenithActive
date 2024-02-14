package com.brvsk.ZenithActive.facility;

import com.brvsk.ZenithActive.excpetion.FacilityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilitiesService;

    @GetMapping
    public ResponseEntity<List<FacilityResponse>> getAllFacilities() {
        try {
            List<FacilityResponse> facilities = facilitiesService.getAllFacilities();
            return ResponseEntity.ok(facilities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityResponse> getFacilityById(@PathVariable UUID facilityId) {
        try {
            FacilityResponse facility = facilitiesService.getFacilityById(facilityId);
            return ResponseEntity.ok(facility);
        } catch (FacilityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Facility> addFacility(@RequestBody @Valid FacilityRequest request) {
        try {
            Facility addedFacility = facilitiesService.addFacility(request);
            return ResponseEntity.ok(addedFacility);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{facilityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFacility(@PathVariable UUID facilityId) {
        try {
            facilitiesService.deleteFacility(facilityId);
            return ResponseEntity.noContent().build();
        } catch (FacilityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
