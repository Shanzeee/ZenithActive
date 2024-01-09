package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.employee.EmployeeType;
import com.brvsk.ZenithActive.utils.validation.ValidEmail;
import com.brvsk.ZenithActive.utils.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterEmployeeRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Employee type cannot be null")
    private EmployeeType employeeType;

    private String additionalInformation;
}
