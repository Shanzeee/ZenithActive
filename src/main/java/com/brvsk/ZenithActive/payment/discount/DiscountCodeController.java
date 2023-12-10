package com.brvsk.ZenithActive.payment.discount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> createDiscountCode(@RequestBody @Valid DiscountCodeCreateRequest request) {
        try {
            discountCodeService.createDiscountCode(request);
            return new ResponseEntity<>("Discount code created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the discount code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteDiscountCode(@PathVariable String code) {
        try {
            discountCodeService.deleteDiscountCode(code);
            return new ResponseEntity<>("Discount code deleted successfully", HttpStatus.OK);
        } catch (DiscountCodeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the discount code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<DiscountCode> getDiscountCode(@PathVariable String code) {
        return discountCodeService.getDiscountCode(code)
                .map(discountCode -> new ResponseEntity<>(discountCode, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/calculate/{code}")
    public ResponseEntity<Integer> calculateDiscountPercentage(@PathVariable String code) {
        try {
            Integer discountPercentage = discountCodeService.calculateDiscountPercentage(code);
            return new ResponseEntity<>(discountPercentage, HttpStatus.OK);
        } catch (DiscountCodeNotFoundException | DiscountCodeNotActiveException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DiscountCode>> getAllDiscountCodes() {
        List<DiscountCode> discountCodes = discountCodeService.getAllDiscountCodes();
        return new ResponseEntity<>(discountCodes, HttpStatus.OK);
    }
}
