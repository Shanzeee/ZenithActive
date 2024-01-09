package com.brvsk.ZenithActive.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register_member")
  public ResponseEntity<AuthenticationResponse> registerMember(@RequestBody @Valid RegisterMemberRequest request) {
    return ResponseEntity.ok(authenticationService.registerMember(request));
  }

  @PostMapping("/register_employee")
  public ResponseEntity<AuthenticationResponse> registerEmployee(@RequestBody @Valid RegisterEmployeeRequest request) {
    return ResponseEntity.ok(authenticationService.registerEmployee(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    authenticationService.refreshToken(request, response);
  }


}
