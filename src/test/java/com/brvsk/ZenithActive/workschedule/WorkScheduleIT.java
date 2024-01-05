package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WorkScheduleIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void addNewWorkSchedule() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setUserId(employeeId);
        employeeRepository.save(employee);

        WorkScheduleCreateRequest request = createWorkScheduleCreateRequest(employeeId);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/workschedules",
                request,
                String.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(workScheduleRepository.existsByEmployee_UserIdAndYearMonth(employeeId, YearMonth.now()));
    }


    @Test
    public void getWorkSchedulesByEmployeeId_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setUserId(employeeId);
        employeeRepository.save(employee);

        WorkScheduleCreateRequest request = createWorkScheduleCreateRequest(employeeId);

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/workschedules",
                request,
                String.class);

        // When
        ResponseEntity<List<WorkScheduleResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/workschedules/" + employeeId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getWorkSchedulesByEmployeeId_UserNotFound() {
        // Given
        UUID nonExistingEmployeeId = UUID.randomUUID();

        // When
        ResponseEntity<List<WorkScheduleResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/workschedules/" + nonExistingEmployeeId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void getWorkScheduleForCurrentMonth_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setUserId(employeeId);
        employeeRepository.save(employee);

        WorkScheduleCreateRequest request = createWorkScheduleCreateRequest(employeeId);

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/workschedules",
                request,
                String.class);

        // When
        ResponseEntity<WorkScheduleResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/workschedules/current/" + employeeId,
                HttpMethod.GET,
                null,
                WorkScheduleResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getWorkScheduleForCurrentMonth_UserNotFound() {
        // Given
        UUID nonExistingEmployeeId = UUID.randomUUID();

        // When
        ResponseEntity<WorkScheduleResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/workschedules/current/" + nonExistingEmployeeId,
                HttpMethod.GET,
                null,
                WorkScheduleResponse.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    private WorkScheduleCreateRequest createWorkScheduleCreateRequest(UUID employeeId) {
        return new WorkScheduleCreateRequest(employeeId,YearMonth.now(),
                Arrays.asList(WorkShift.MORNING,WorkShift.AFTERNOON,WorkShift.OFF,WorkShift.MIDDAY));
    }
}
