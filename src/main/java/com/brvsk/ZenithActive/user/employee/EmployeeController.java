package com.brvsk.ZenithActive.user.employee;


import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable UUID userId) {
        try {
            EmployeeResponse employeeResponse = employeeService.getEmployeeById(userId);
            return ResponseEntity.ok(employeeResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable UUID userId) {
        try {
            employeeService.deleteEmployee(userId);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }
}

