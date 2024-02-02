package com.brvsk.ZenithActive.diet;

import com.brvsk.ZenithActive.diet.dietprofile.DietRequest;
import com.brvsk.ZenithActive.diet.mealprofile.MealProfileResponseSimple;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @PostMapping("/create")
    public ResponseEntity<String> createDiet(@RequestBody @Valid DietRequest dietRequest) {
        try {
            dietService.createDiet(dietRequest);
            return new ResponseEntity<>("Diet created successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<DietResponse>> getDietsForMember(@PathVariable UUID memberId) {
        try {
            List<DietResponse> diets = dietService.getDietsForMember(memberId);
            return new ResponseEntity<>(diets, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/meals/member/{memberId}")
    public ResponseEntity<List<MealProfileResponseSimple>> getMealsForDiets(@PathVariable UUID memberId) {
        try {
            List<MealProfileResponseSimple> meals = dietService.getMealsForDiets(memberId);
            return new ResponseEntity<>(meals, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
