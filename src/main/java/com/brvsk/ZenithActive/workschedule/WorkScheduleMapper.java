package com.brvsk.ZenithActive.workschedule;

import org.springframework.stereotype.Component;

@Component
public class WorkScheduleMapper {

    public WorkScheduleResponse mapToResponse(WorkSchedule workSchedule){
        return WorkScheduleResponse.builder()
                .workScheduleId(workSchedule.getWorkScheduleId())
                .employeeId(workSchedule.getEmployee().getUserId())
                .employeeType(workSchedule.getEmployee().getEmployeeType())
                .employeeName(workSchedule.getEmployee().getFirstName()+" "+workSchedule.getEmployee().getLastName())
                .yearMonth(workSchedule.getYearMonth())
                .shifts(workSchedule.getShifts())
                .build();
    }
}
