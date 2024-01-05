package com.brvsk.ZenithActive.workschedule;


import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class WorkScheduleCreateRequest {

    private final UUID employeeId;
    private final YearMonth yearMonth;
    private final List<WorkShift> workShifts;
}
