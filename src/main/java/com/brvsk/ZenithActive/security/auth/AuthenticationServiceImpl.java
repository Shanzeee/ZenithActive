package com.brvsk.ZenithActive.security.auth;


import com.brvsk.ZenithActive.notification.newsletter.NewsletterService;
import com.brvsk.ZenithActive.security.config.JwtService;
import com.brvsk.ZenithActive.security.token.Token;
import com.brvsk.ZenithActive.security.token.TokenRepository;
import com.brvsk.ZenithActive.security.token.TokenType;
import com.brvsk.ZenithActive.user.EmailAlreadyExistsException;
import com.brvsk.ZenithActive.user.Role;
import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.user.UserRepository;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
  private final UserRepository userRepository;
  private final MemberRepository memberRepository;
  private final EmployeeRepository employeeRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final NewsletterService newsletterService;

  @Override
  public AuthenticationResponse registerMember(RegisterMemberRequest request) {
    checkIfEmailExists(request.getEmail());

    Member member = toMemberEntity(request);
    var savedMember = memberRepository.save(member);
    var jwtToken = jwtService.generateToken(member);
    var refreshToken = jwtService.generateRefreshToken(member);
    saveUserToken(savedMember, jwtToken);
    handleNewsletterSubscription(request);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Override
  public AuthenticationResponse registerEmployee(RegisterEmployeeRequest request){
    checkIfEmailExists(request.getEmail());

    Employee employee = toEmployeeEntity(request);
    var savedEmployee = employeeRepository.save(employee);
    var jwtToken = jwtService.generateToken(employee);
    var refreshToken = jwtService.generateRefreshToken(employee);
    saveUserToken(savedEmployee, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Override
  public void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  @Override
  public void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllByUserUserIdAndRevokedIsFalseAndExpiredIsFalse(user.getUserId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  @Override
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userRepository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  @Override
  public void checkIfEmailExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException(email);
    }
  }

  @Override
  public Member toMemberEntity(RegisterMemberRequest request){
    Member member = new Member();
    member.setUserId(UUID.randomUUID());
    member.setFirstName(request.getFirstName());
    member.setLastName(request.getLastName());
    member.setGender(request.getGender());
    member.setEmail(request.getEmail());
    member.setPassword(passwordEncoder.encode(request.getPassword()));
    member.setRole(Role.MEMBER);
    member.setHeight(request.getHeight());
    member.setWeight(request.getWeight());
    return member;
  }

  @Override
  public Employee toEmployeeEntity(RegisterEmployeeRequest request){
    Employee employee = new Employee();
    employee.setUserId(UUID.randomUUID());
    employee.setFirstName(request.getFirstName());
    employee.setLastName(request.getLastName());
    employee.setGender(request.getGender());
    employee.setEmail(request.getEmail());
    employee.setPassword(passwordEncoder.encode(request.getPassword()));
    employee.setRole(Role.EMPLOYEE);
    employee.setEmployeeType(request.getEmployeeType());
    employee.setHireDate(LocalDate.now());
    employee.setAdditionalInformation(request.getAdditionalInformation());
    return employee;
  }

  private void handleNewsletterSubscription(RegisterMemberRequest request){
    if (request.isEmailNewsletter()) {
      newsletterService.subscribeNewsletter(request.getFirstName(), request.getEmail());
    }
  }


}
