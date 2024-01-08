package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.employee.EmployeeType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterEmployeeRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Gender gender;
    private EmployeeType employeeType;
    private String additionalInformation;
}
