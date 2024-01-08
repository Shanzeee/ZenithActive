package com.brvsk.ZenithActive.user.employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(UUID userId);
    void deleteEmployee(UUID userId);
}
