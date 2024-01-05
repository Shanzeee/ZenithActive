package com.brvsk.ZenithActive.workschedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkScheduleNotFound extends RuntimeException{
    public WorkScheduleNotFound(String message) {
        super(message);
    }
}
