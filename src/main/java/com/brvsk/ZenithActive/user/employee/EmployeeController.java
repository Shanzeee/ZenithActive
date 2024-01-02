package com.brvsk.ZenithActive.user.employee;


import com.brvsk.ZenithActive.user.EmailAlreadyExistsException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import jakarta.validation.Valid;
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

    @PostMapping("/create")
    public ResponseEntity<String> createEmployee(@RequestBody @Valid EmployeeCreateRequest request) {
        try {
            employeeService.createEmployee(request);
            return ResponseEntity.ok("Employee created successfully");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
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

