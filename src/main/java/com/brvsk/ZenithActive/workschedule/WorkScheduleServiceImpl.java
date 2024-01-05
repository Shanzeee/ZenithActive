package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleServiceImpl implements WorkScheduleService{

    private final WorkScheduleRepository workScheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkScheduleMapper workScheduleMapper;
    private final EmailSender emailSender;

    @Override
    public void addNewWorkSchedule(WorkScheduleCreateRequest request) {
        Employee employee = getEmployeeById(request.getEmployeeId());

        WorkSchedule workSchedule = toEntity(request);
        workSchedule.setEmployee(employee);

        workScheduleRepository.save(workSchedule);

        emailSender.sendNewWorkScheduleNotification(employee.getEmail(), employee.getFirstName(), request.getYearMonth());
    }

    @Override
    public List<WorkScheduleResponse> getWorkSchedulesByEmployeeId(UUID employeeId) {
        Employee employee = getEmployeeById(employeeId);

        List<WorkSchedule> workSchedules = workScheduleRepository.findWorkSchedulesByEmployee(employee)
                .orElseThrow(() -> new WorkScheduleNotFound("WorkSchedule not found for employee with id: " + employeeId));

        return mapWorkSchedulesToResponse(workSchedules);
    }

    @Override
    public WorkScheduleResponse getWorkScheduleForCurrentMonth(UUID employeeId) {
        YearMonth currentYearMonth = YearMonth.now();
        WorkSchedule workSchedule = getWorkScheduleByEmployeeAndYearMonth(employeeId, currentYearMonth);

        return workScheduleMapper.mapToResponse(workSchedule);
    }

    @Override
    public WorkScheduleResponse getWorkScheduleForNextMonth(UUID employeeId) {
        YearMonth nextYearMonth = YearMonth.now().plusMonths(1);
        WorkSchedule workSchedule = getWorkScheduleByEmployeeAndYearMonth(employeeId, nextYearMonth);

        return workScheduleMapper.mapToResponse(workSchedule);
    }

    @Override
    public List<WorkScheduleResponse> getWorkSchedulesForAllEmployeesInMonth(YearMonth yearMonth) {
        List<WorkSchedule> allWorkSchedulesInMonth = workScheduleRepository.findWorkSchedulesByYearMonth(yearMonth);

        return allWorkSchedulesInMonth.stream()
                .map(workScheduleMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public int calculateWorkedHoursInMonthForEmployee(UUID employeeId, YearMonth yearMonth) {
        WorkSchedule workSchedule = getWorkScheduleByEmployeeAndYearMonth(employeeId, yearMonth);
        return calculateWorkedHoursForWorkSchedule(workSchedule);
    }

    @Override
    public void editWorkSchedule(UUID employeeId, YearMonth yearMonth, WorkScheduleEditRequest request) {
        validateWorkScheduleEdit(yearMonth);

        Employee employee = getEmployeeById(employeeId);
        WorkSchedule existingWorkSchedule = getWorkScheduleByEmployeeAndYearMonth(employeeId, yearMonth);

        existingWorkSchedule.setShifts(request.getWorkShifts());

        workScheduleRepository.save(existingWorkSchedule);

        emailSender.sendUpdatedWorkScheduleNotification(employee.getEmail(),employee.getFirstName(),yearMonth);
    }

    private int calculateWorkedHoursForWorkSchedule(WorkSchedule workSchedule) {
        return workSchedule.getShifts().stream()
                .mapToInt(WorkShift::getWorkingHours)
                .sum();
    }

    private WorkSchedule toEntity (WorkScheduleCreateRequest request) {
        return WorkSchedule.builder()
                .yearMonth(request.getYearMonth())
                .shifts(request.getWorkShifts())
                .build();
    }

    private Employee getEmployeeById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new UserNotFoundException(employeeId));
    }

    private WorkSchedule getWorkScheduleByEmployeeAndYearMonth(UUID employeeId, YearMonth yearMonth) {
        getEmployeeById(employeeId);
        return workScheduleRepository.findWorkScheduleByEmployee_UserIdAndYearMonth(employeeId, yearMonth)
                .orElseThrow(() -> new WorkScheduleNotFound("WorkSchedule not found for employee in the specified month"));
    }

    private List<WorkScheduleResponse> mapWorkSchedulesToResponse(List<WorkSchedule> workSchedules) {
        return workSchedules.stream()
                .map(workScheduleMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateWorkScheduleEdit(YearMonth yearMonth) {
        YearMonth currentYearMonth = YearMonth.now();

        if (yearMonth.isBefore(currentYearMonth)) {
            throw new IllegalArgumentException("Cannot edit work schedule for past months.");
        }
    }


}
