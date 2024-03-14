package com.brvsk.ZenithActive.diet.mealprefernce;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/member-meal-preferences")
@RequiredArgsConstructor
public class MemberMealPreferenceController {

    private final MemberMealPreferenceService memberMealPreferenceService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createMemberMealPreference(@RequestBody MemberMealPreferenceCreateRequest request) {
        try {
            memberMealPreferenceService.createMemberMealPreference(request);
            return new ResponseEntity<>("MemberMealPreference created successfully", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteMemberMealPreference(@PathVariable UUID id) {
        try {
            memberMealPreferenceService.deleteMemberMealPreference(id);
            return new ResponseEntity<>("MemberMealPreference deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
