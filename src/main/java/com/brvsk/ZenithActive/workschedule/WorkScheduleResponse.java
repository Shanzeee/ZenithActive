package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.employee.EmployeeType;
import lombok.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkScheduleResponse {

    private UUID workScheduleId;
    private UUID employeeId;
    private EmployeeType employeeType;
    private String employeeName;
    private YearMonth yearMonth;
    private List<WorkShift> shifts;

}
