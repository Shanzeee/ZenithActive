package com.brvsk.ZenithActive.payment.discount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discount-codes")
@RequiredArgsConstructor
public class DiscountCodeController {

    private final DiscountCodeService discountCodeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createDiscountCode(@RequestBody @Valid DiscountCodeCreateRequest request) {
        discountCodeService.createDiscountCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Discount code created successfully");
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiscountCode(@PathVariable String code) {
        try {
            discountCodeService.deleteDiscountCode(code);
            return ResponseEntity.ok("Discount code deleted successfully");
        } catch (DiscountCodeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the discount code");
        }
    }

    @GetMapping("/calculate/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> calculateDiscountPercentage(@PathVariable String code) {
        try {
            Integer discountPercentage = discountCodeService.calculateDiscountPercentage(code);
            return ResponseEntity.ok(discountPercentage);
        } catch (DiscountCodeNotFoundException | DiscountCodeNotActiveException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DiscountCode>> getAllDiscountCodes() {
        List<DiscountCode> discountCodes = discountCodeService.getAllDiscountCodes();
        return ResponseEntity.ok(discountCodes);
    }
}
