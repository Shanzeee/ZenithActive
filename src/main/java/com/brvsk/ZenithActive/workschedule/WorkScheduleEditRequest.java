package com.brvsk.ZenithActive.workschedule;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorkScheduleEditRequest {
    private final List<WorkShift> workShifts;
}
