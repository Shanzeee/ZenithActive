package com.brvsk.ZenithActive.user.employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(UUID userId);
    void createEmployee(EmployeeCreateRequest employeeCreateRequest);
    void deleteEmployee(UUID userId);
}
