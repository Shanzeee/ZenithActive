package com.brvsk.ZenithActive.facility;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        List<FacilityResponse> facilities = facilitiesService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityResponse> getFacilityById(@PathVariable UUID facilityId) {
        FacilityResponse facility = facilitiesService.getFacilityById(facilityId);
        return ResponseEntity.ok(facility);
    }

    @PostMapping
    public ResponseEntity<Facility> addFacility(@RequestBody @Valid FacilityRequest request) {
        Facility addedFacility = facilitiesService.addFacility(request);
        return ResponseEntity.ok(addedFacility);
    }

    @DeleteMapping("/{facilityId}")
    public ResponseEntity<Void> deleteFacility(@PathVariable UUID facilityId) {
        facilitiesService.deleteFacility(facilityId);
        return ResponseEntity.noContent().build();
    }
}
