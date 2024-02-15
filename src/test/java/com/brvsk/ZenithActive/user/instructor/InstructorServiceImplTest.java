package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.user.UserRepository;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InstructorServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private InstructorServiceImpl instructorService;

    @Test
    void createNewInstructorFromEmployee_Valid() {
        // Given
        UUID userId = UUID.randomUUID();
        Employee employee = mock(Employee.class);
        when(employeeRepository.findById(userId)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(new Instructor());

        InstructorCreateRequest request = createRequest();
        request.setUserId(userId);

        // When
        assertDoesNotThrow(() -> instructorService.createNewInstructorFromEmployee(request));

        // Then
        verify(employeeRepository, times(1)).findById(userId);
        verify(employeeRepository, times(1)).delete(any(Employee.class));
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void createNewInstructorFromEmployee_ThrowUserNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        when(employeeRepository.findById(userId)).thenReturn(Optional.empty());

        InstructorCreateRequest request = createRequest();
        request.setUserId(userId);

        // When, Then
        assertThrows(UserNotFoundException.class, () -> instructorService.createNewInstructorFromEmployee(request));
    }

    @Test
    void deleteInstructor_Valid() {
        // Given
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = mock(Instructor.class);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        doNothing().when(instructorRepository).deleteById(instructorId);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // When
        assertDoesNotThrow(() -> instructorService.deleteInstructor(instructorId));

        // Then
        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).deleteById(instructorId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteInstructor_ThrowUserNotFoundException() {
        // Given
        UUID instructorId = UUID.randomUUID();
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> instructorService.deleteInstructor(instructorId));
    }

    private InstructorCreateRequest createRequest() {
        return InstructorCreateRequest.builder()
                .specialities(List.of(Speciality.PILATES, Speciality.PILATES))
                .build();
    }
}