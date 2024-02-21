package com.brvsk.ZenithActive.security.auth;

import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse registerMember(RegisterMemberRequest request);

    AuthenticationResponse registerEmployee(RegisterEmployeeRequest request);

    AuthenticationResponse registerInstructor(RegisterInstructorRequest request);

    AuthenticationResponse createAdmin();

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    void checkIfEmailExists(String email);

    Member toMemberEntity(RegisterMemberRequest request);

    Employee toEmployeeEntity(RegisterEmployeeRequest request);

    Instructor toInstructorEntity(RegisterInstructorRequest request);
}
