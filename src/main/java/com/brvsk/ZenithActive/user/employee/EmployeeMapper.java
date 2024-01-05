package com.brvsk.ZenithActive.user.employee;

import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .userId(employee.getUserId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .gender(employee.getGender())
                .employeeType(employee.getEmployeeType())
                .hireDate(employee.getHireDate())
                .additionalInformation(employee.getAdditionalInformation())
                .build();
    }
}
