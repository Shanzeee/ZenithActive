package com.brvsk.ZenithActive.trainingplan.create;

import com.brvsk.ZenithActive.trainingplan.create.dto.TrainingPlanCreateRequest;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> createTrainingPlan(@RequestBody TrainingPlanCreateRequest trainingPlanCreateRequest) {
        try {
            trainingPlanService.createTrainingPlan(trainingPlanCreateRequest);
            return ResponseEntity.ok("Training Plan created successfully");
        } catch (UserNotFoundException | TrainingPlanRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the training plan");
        }
    }

    @GetMapping("/{memberId}/{trainingPlanRequestId}/pdf")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<byte[]> getTrainingPlanPdf(@PathVariable UUID memberId, @PathVariable UUID trainingPlanRequestId) {
        byte[] pdfContent = trainingPlanService.getTrainingPlanPdf(memberId, trainingPlanRequestId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"training-plan.pdf\"")
                .body(pdfContent);
    }
}
