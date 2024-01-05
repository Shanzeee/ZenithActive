package com.brvsk.ZenithActive.workschedule;

import java.time.LocalTime;

public enum WorkShift {
    MORNING(LocalTime.of(6, 0), LocalTime.of(14, 0), 8),
    MIDDAY(LocalTime.of(12, 0), LocalTime.of(18, 0), 6),
    AFTERNOON(LocalTime.of(14, 0), LocalTime.of(22, 0), 8),
    OFF(null, null, 0);

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int workingHours;

    WorkShift(LocalTime startTime, LocalTime endTime, int workingHours) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workingHours = workingHours;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getWorkingHours() {
        return workingHours;
    }
}
