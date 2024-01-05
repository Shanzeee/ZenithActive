package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workschedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @PostMapping
    public ResponseEntity<String> addNewWorkSchedule(@RequestBody WorkScheduleCreateRequest request) {
        try {
            workScheduleService.addNewWorkSchedule(request);
            return new ResponseEntity<>("Work schedule added successfully", HttpStatus.CREATED);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesByEmployeeId(@PathVariable UUID employeeId) {
        try {
            List<WorkScheduleResponse> workSchedules = workScheduleService.getWorkSchedulesByEmployeeId(employeeId);
            return ResponseEntity.ok(workSchedules);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current/{employeeId}")
    public ResponseEntity<WorkScheduleResponse> getWorkScheduleForCurrentMonth(@PathVariable UUID employeeId) {
        try {
            WorkScheduleResponse workSchedule = workScheduleService.getWorkScheduleForCurrentMonth(employeeId);
            return ResponseEntity.ok(workSchedule);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/next/{employeeId}")
    public ResponseEntity<WorkScheduleResponse> getWorkScheduleForNextMonth(@PathVariable UUID employeeId) {
        try {
            WorkScheduleResponse workSchedule = workScheduleService.getWorkScheduleForNextMonth(employeeId);
            return ResponseEntity.ok(workSchedule);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/{year}/{month}")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesForAllEmployeesInMonth(
            @PathVariable int year, @PathVariable int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            List<WorkScheduleResponse> workSchedules = workScheduleService.getWorkSchedulesForAllEmployeesInMonth(yearMonth);
            return ResponseEntity.ok(workSchedules);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hours/{employeeId}/{year}/{month}")
    public ResponseEntity<Integer> calculateWorkedHoursInMonthForEmployee(
            @PathVariable UUID employeeId, @PathVariable int year, @PathVariable int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            int workedHours = workScheduleService.calculateWorkedHoursInMonthForEmployee(employeeId, yearMonth);
            return ResponseEntity.ok(workedHours);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{employeeId}/{year}/{month}")
    public ResponseEntity<String> editWorkSchedule(
            @PathVariable UUID employeeId, @PathVariable int year, @PathVariable int month,
            @RequestBody WorkScheduleEditRequest request) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            workScheduleService.editWorkSchedule(employeeId, yearMonth, request);
            return ResponseEntity.ok("Work schedule edited successfully");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | WorkScheduleNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
