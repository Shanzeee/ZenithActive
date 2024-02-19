package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
