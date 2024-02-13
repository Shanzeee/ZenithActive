package com.brvsk.ZenithActive.loyalty;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loyalty-points")
@RequiredArgsConstructor
public class LoyaltyPointsController {

    private final LoyaltyPointsService loyaltyPointsService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> addLoyaltyPoints(@RequestBody @Valid LoyaltyPointsCreateRequest request) {
        try {
            loyaltyPointsService.addLoyaltyPoints(request);
            return ResponseEntity.ok("Loyalty points added successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
        }
    }

    @GetMapping("/total-points-new/{memberId}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<Integer> getTotalPointsForMember(@PathVariable UUID memberId) {
        try {
            int totalPoints = loyaltyPointsService.getTotalPointsForMember(memberId);
            return ResponseEntity.ok(totalPoints);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/count-given-points-today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countGivenPointsToday() {
        try {
            long count = loyaltyPointsService.countGivenPointsToday();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
