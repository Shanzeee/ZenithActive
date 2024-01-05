package com.brvsk.ZenithActive.workschedule;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface WorkScheduleService {

    void addNewWorkSchedule(WorkScheduleCreateRequest request);

    List<WorkScheduleResponse> getWorkSchedulesByEmployeeId(UUID employeeId);

    WorkScheduleResponse getWorkScheduleForCurrentMonth(UUID employeeId);

    WorkScheduleResponse getWorkScheduleForNextMonth(UUID employeeId);

    List<WorkScheduleResponse> getWorkSchedulesForAllEmployeesInMonth(YearMonth yearMonth);

    int calculateWorkedHoursInMonthForEmployee(UUID employeeId, YearMonth yearMonth);

    void editWorkSchedule(UUID employeeId, YearMonth yearMonth, WorkScheduleEditRequest request);
}
