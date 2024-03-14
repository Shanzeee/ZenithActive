package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponseDetailedInfo;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/training-plan-requests")
@RequiredArgsConstructor
public class TrainingPlanRequestController {

    private final TrainingPlanRequestService trainingPlanRequestService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> createTrainingPlanRequest(@RequestBody TrainingPlanRequestCreateCommand command) {
        try {
            trainingPlanRequestService.createTrainingPlanRequest(command);
            return ResponseEntity.ok("Training Plan Request created successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with provided ID.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<TrainingPlanRequestResponse>> getPendingTrainingPlanRequests() {
        try {
            List<TrainingPlanRequestResponse> pendingRequests =
                    trainingPlanRequestService.getPendingTrainingPlanRequests();
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/pending/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<TrainingPlanRequestResponseDetailedInfo> getTrainingPlanRequestById(@PathVariable UUID id) {
        try {
            TrainingPlanRequestResponseDetailedInfo requestDetails =
                    trainingPlanRequestService.getTrainingPlanRequestById(id);
            return ResponseEntity.ok(requestDetails);
        } catch (TrainingPlanRequestNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
