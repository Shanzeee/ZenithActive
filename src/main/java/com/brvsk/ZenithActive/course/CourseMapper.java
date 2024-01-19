package com.brvsk.ZenithActive.course;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CourseMapper {

    public CourseResponse mapToResponse(Course course){

        return CourseResponse.builder()
                .courseId(course.getId())
                .courseType(course.getCourseType())
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .build();
    }
}