package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class WorkScheduleServiceImplTest {

    @Mock
    private WorkScheduleRepository workScheduleRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private WorkScheduleMapper workScheduleMapper;
    @Mock
    private EmailSender emailSender;
    @InjectMocks
    private WorkScheduleServiceImpl workScheduleService;

    @Test
    public void addNewWorkSchedule_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        WorkScheduleCreateRequest request = createWorkScheduleCreateRequest(employeeId);

        Employee employee = new Employee();
        employee.setUserId(request.getEmployeeId());

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(workScheduleRepository.save(any())).thenReturn(new WorkSchedule());

        workScheduleService.addNewWorkSchedule(request);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void addNewWorkSchedule_ThrowEmployeeNotFoundException() {
        // Given
        UUID employeeId = UUID.randomUUID();
        WorkScheduleCreateRequest request = createWorkScheduleCreateRequest(employeeId);

        // When, Then
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> workScheduleService.addNewWorkSchedule(request));
    }

    @Test
    public void getWorkSchedulesByEmployeeId_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setUserId(employeeId);

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkSchedulesByEmployee(any())).thenReturn(Optional.of(Arrays.asList(new WorkSchedule(), new WorkSchedule())));

        List<WorkScheduleResponse> workSchedules = workScheduleService.getWorkSchedulesByEmployeeId(employeeId);

        // Then
        assertEquals(2, workSchedules.size());
    }

    @Test
    public void getWorkScheduleForCurrentMonth_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        YearMonth currentYearMonth = YearMonth.now();
        WorkSchedule workSchedule = new WorkSchedule();

        // When
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(java.util.Optional.of(workSchedule));
        when(workScheduleMapper.mapToResponse(any())).thenReturn(new WorkScheduleResponse());

        WorkScheduleResponse result = workScheduleService.getWorkScheduleForCurrentMonth(employeeId);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).findWorkScheduleByEmployee_UserIdAndYearMonth(employeeId, currentYearMonth);
        Mockito.verify(workScheduleMapper, Mockito.times(1)).mapToResponse(workSchedule);
        assertNotNull(result);

    }

    @Test
    public void getWorkScheduleForNextMonth_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        YearMonth nextYearMonth = YearMonth.now().plusMonths(1);
        WorkSchedule workSchedule = new WorkSchedule();

        // When
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.of(workSchedule));
        when(workScheduleMapper.mapToResponse(any())).thenReturn(new WorkScheduleResponse());

        WorkScheduleResponse result = workScheduleService.getWorkScheduleForNextMonth(employeeId);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).findWorkScheduleByEmployee_UserIdAndYearMonth(employeeId, nextYearMonth);
        Mockito.verify(workScheduleMapper, Mockito.times(1)).mapToResponse(workSchedule);
        assertNotNull(result);
    }

    @Test
    public void getWorkSchedulesForAllEmployeesInMonth_Valid() {
        // Given
        YearMonth yearMonth = YearMonth.of(2023, 1);
        List<WorkSchedule> allWorkSchedulesInMonth = Arrays.asList(new WorkSchedule(), new WorkSchedule());

        // When
        when(workScheduleRepository.findWorkSchedulesByYearMonth(yearMonth)).thenReturn(allWorkSchedulesInMonth);

        List<WorkScheduleResponse> result = workScheduleService.getWorkSchedulesForAllEmployeesInMonth(yearMonth);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).findWorkSchedulesByYearMonth(yearMonth);
        Mockito.verify(workScheduleMapper, Mockito.times(2)).mapToResponse(any());
        assertEquals(2, result.size());
    }

    @Test
    public void calculateWorkedHoursInMonthForEmployee_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        YearMonth yearMonth = YearMonth.of(2023, 1);
        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setShifts(Arrays.asList(WorkShift.MORNING, WorkShift.MIDDAY));

        // When
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.of(workSchedule));

        int result = workScheduleService.calculateWorkedHoursInMonthForEmployee(employeeId, yearMonth);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).findWorkScheduleByEmployee_UserIdAndYearMonth(employeeId, yearMonth);
        assertEquals(14, result);
    }

    @Test
    public void editWorkSchedule_Valid() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth yearMonth = YearMonth.now().plusYears(1);
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));
        Employee employee = new Employee();
        WorkSchedule existingWorkSchedule = new WorkSchedule();

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.of(existingWorkSchedule));
        when(workScheduleRepository.save(any())).thenReturn(existingWorkSchedule);

        workScheduleService.editWorkSchedule(employeeId, yearMonth, request);

        // Then
        Mockito.verify(workScheduleRepository, Mockito.times(1)).findWorkScheduleByEmployee_UserIdAndYearMonth(employeeId, yearMonth);
        Mockito.verify(workScheduleRepository, Mockito.times(1)).save(existingWorkSchedule);
        assertEquals(request.getWorkShifts(), existingWorkSchedule.getShifts());
    }

    @Test
    public void getWorkSchedulesByEmployeeId_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> workScheduleService.getWorkSchedulesByEmployeeId(employeeId));
    }

    @Test
    public void getWorkScheduleForCurrentMonth_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();

        // When
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> workScheduleService.getWorkScheduleForCurrentMonth(employeeId));
    }

    @Test
    public void getWorkScheduleForNextMonth_WorkScheduleNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();

        // When
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.empty());

        // Then
        assertThrows(WorkScheduleNotFound.class, () -> workScheduleService.getWorkScheduleForNextMonth(employeeId));
    }

    @Test
    public void getWorkSchedulesForAllEmployeesInMonth_NoWorkSchedules() {
        // Given
        YearMonth yearMonth = YearMonth.of(2023, 1);

        // When
        when(workScheduleRepository.findWorkSchedulesByYearMonth(yearMonth)).thenReturn(Collections.emptyList());

        // Then
        List<WorkScheduleResponse> result = workScheduleService.getWorkSchedulesForAllEmployeesInMonth(yearMonth);
        assertEquals(0, result.size());
    }

    @Test
    public void calculateWorkedHoursInMonthForEmployee_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth yearMonth = YearMonth.of(2023, 1);

        // When
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> workScheduleService.calculateWorkedHoursInMonthForEmployee(employeeId, yearMonth));
    }

    @Test
    public void calculateWorkedHoursInMonthForEmployee_WorkScheduleNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        YearMonth yearMonth = YearMonth.of(2023, 1);

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.empty());

        // Then
        assertThrows(WorkScheduleNotFound.class, () -> workScheduleService.calculateWorkedHoursInMonthForEmployee(employeeId, yearMonth));
    }

    @Test
    public void editWorkSchedule_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth yearMonth = YearMonth.now().plusYears(1);
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> workScheduleService.editWorkSchedule(employeeId, yearMonth, request));
    }

    @Test
    public void editWorkSchedule_WorkScheduleNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth yearMonth = YearMonth.now().plusYears(1);
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));
        Employee employee = new Employee();

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.empty());

        // Then
        assertThrows(WorkScheduleNotFound.class, () -> workScheduleService.editWorkSchedule(employeeId, yearMonth, request));
    }

    @Test
    public void editWorkSchedule_PastYearMonth() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth pastYearMonth = YearMonth.now().minusMonths(1);
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> workScheduleService.editWorkSchedule(employeeId, pastYearMonth, request));
    }

    @Test
    public void editWorkSchedule_CurrentYearMonth() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth currentYearMonth = YearMonth.now();
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.of(new WorkSchedule()));

        // Then
        assertDoesNotThrow(() -> workScheduleService.editWorkSchedule(employeeId, currentYearMonth, request));
    }

    @Test
    public void editWorkSchedule_FutureYearMonth() {
        // Given
        UUID employeeId = UUID.randomUUID();
        YearMonth futureYearMonth = YearMonth.now().plusYears(1);
        WorkScheduleEditRequest request = new WorkScheduleEditRequest(Arrays.asList(WorkShift.MORNING, WorkShift.AFTERNOON));

        // When
        when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
        when(workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(any(), any())).thenReturn(Optional.of(new WorkSchedule()));
        when(workScheduleRepository.save(any())).thenReturn(new WorkSchedule());

        // Then
        assertDoesNotThrow(() -> workScheduleService.editWorkSchedule(employeeId, futureYearMonth, request));
    }

    private WorkScheduleCreateRequest createWorkScheduleCreateRequest(UUID employeeId) {
        return new WorkScheduleCreateRequest(employeeId,YearMonth.now(),
                Arrays.asList(WorkShift.MORNING,WorkShift.AFTERNOON,WorkShift.OFF,WorkShift.MIDDAY));
    }
}