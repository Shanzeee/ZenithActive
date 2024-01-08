package com.brvsk.ZenithActive.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.brvsk.ZenithActive.user.Permission.*;
import static com.brvsk.ZenithActive.user.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "v3/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/csa/api/token",
            "/actuator/**",
            "/health/**"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, "/api/v1/facilities/**").permitAll()
                                .requestMatchers(POST,"/api/v1/facilities/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/facilities/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(POST, "/api/v1/courses").hasAnyRole(INSTRUCTOR.name(),ADMIN.name())
                                .requestMatchers(POST, "/api/v1/courses/enroll").hasAnyRole(MEMBER.name())
                                .requestMatchers(GET, "/api/v1/courses/**").permitAll()
                                .requestMatchers(POST, "/api/v1/loyalty-points/**").hasAnyRole(INSTRUCTOR.name(),ADMIN.name())
                                .requestMatchers("/api/v1/loyalty-points/total-points-new/**").hasAnyRole(MEMBER.name())
                                .requestMatchers(GET, "/api/v1/loyalty-points/total-points-new/**").hasAnyAuthority(MEMBER_READ.name())
                                .requestMatchers(GET ,"/api/v1/loyalty-points/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/membership/**").hasAnyRole(MEMBER.name(), ADMIN.name())
                                .requestMatchers("/api/v1/newsletter").permitAll()
                                .requestMatchers("/api/v1/discount-codes/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/qrcode/**").hasAnyRole(EMPLOYEE.name(),INSTRUCTOR.name(), MEMBER.name())
                                .requestMatchers(POST,"/api/v1/reviews/**").hasAnyRole(MEMBER.name())
                                .requestMatchers(GET,"/api/v1/reviews/**").permitAll()
                                .requestMatchers(POST, "/api/v1/training-plan-requests/**").hasAnyRole(MEMBER.name())
                                .requestMatchers(GET, "/api/v1/training-plan-requests/**").hasAnyRole(INSTRUCTOR.name())
                                .requestMatchers("/api/v1/training-plans/**").hasAnyRole(INSTRUCTOR.name())
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }
}
