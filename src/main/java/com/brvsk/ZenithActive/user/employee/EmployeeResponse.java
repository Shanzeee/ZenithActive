package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.user.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class EmployeeResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private EmployeeType employeeType;
    private LocalDate hireDate;
    private String additionalInformation;
}
