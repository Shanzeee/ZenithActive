package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployees_Valid() {
        // Given
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.mapToResponse(any(Employee.class))).thenReturn(new EmployeeResponse());

        // When
        List<EmployeeResponse> result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(2)).mapToResponse(any(Employee.class));
    }

    @Test
    void getEmployeeById_Valid() {
        // Given
        UUID userId = UUID.randomUUID();
        Employee employee = new Employee();
        when(employeeRepository.findById(userId)).thenReturn(Optional.of(employee));
        when(employeeMapper.mapToResponse(employee)).thenReturn(new EmployeeResponse());

        // When
        EmployeeResponse result = employeeService.getEmployeeById(userId);

        // Then
        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(userId);
        verify(employeeMapper, times(1)).mapToResponse(employee);
    }

    @Test
    void getEmployeeById_ThrowUserNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        when(employeeRepository.findById(userId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> employeeService.getEmployeeById(userId));
    }

    @Test
    void deleteEmployee_Valid() {
        // Given
        UUID userId = UUID.randomUUID();
        doNothing().when(employeeRepository).deleteById(userId);

        // When
        employeeService.deleteEmployee(userId);

        // Then
        verify(employeeRepository, times(1)).deleteById(userId);
    }


}