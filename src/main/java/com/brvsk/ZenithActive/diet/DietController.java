package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @PostMapping("/createOneDayDiet")
    public ResponseEntity<String> createOneDayDiet(@RequestBody @Valid DietRequest dietRequest) {
        try {
            dietService.createOneDayDiet(dietRequest);
            return new ResponseEntity<>("Diet created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
