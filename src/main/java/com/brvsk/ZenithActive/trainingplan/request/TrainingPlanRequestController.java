package com.brvsk.ZenithActive.trainingplan.request;

import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestCreateCommand;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponse;
import com.brvsk.ZenithActive.trainingplan.request.dto.TrainingPlanRequestResponseDetailedInfo;
import com.brvsk.ZenithActive.trainingplan.request.exception.TrainingPlanRequestNotFoundException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/training-plan-requests")
@RequiredArgsConstructor
public class TrainingPlanRequestController {

    private final TrainingPlanRequestService trainingPlanRequestService;

    @PostMapping("/create")
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
    public ResponseEntity<TrainingPlanRequestResponseDetailedInfo> getTrainingPlanRequestById(@PathVariable UUID id) {
        try {
            TrainingPlanRequestResponseDetailedInfo requestDetails =
                    trainingPlanRequestService.getTrainingPlanRequestById(id);
            return ResponseEntity.ok(requestDetails);
        } catch (TrainingPlanRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
