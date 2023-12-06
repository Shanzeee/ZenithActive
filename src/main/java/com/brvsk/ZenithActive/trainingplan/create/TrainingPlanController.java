package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @PostMapping("/create")
    public ResponseEntity<String> crateTrainingPlanRequest(@RequestBody TrainingPlanCreateRequest trainingPlanCreateRequest) {
        trainingPlanService.createTrainingPlan(trainingPlanCreateRequest);
        return ResponseEntity.ok("Training Plan created successfully");
    }
}
