package com.brvsk.ZenithActive.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(final UUID courseId) {
        super("Course with id "+courseId+" not found");
    }
}