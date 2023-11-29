package com.brvsk.ZenithActive.course;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CourseMapper {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public CourseResponse mapToResponse(Course course){
        String formattedStartTime = course.getStartTime().format(timeFormatter);
        String formattedEndTime = course.getEndTime().format(timeFormatter);
        String formattedDuration = formattedStartTime + " - " + formattedEndTime;

        return CourseResponse.builder()
                .courseId(course.getId())
                .courseType(course.getCourseType())
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .groupSize(course.getGroupSize())
                .currentEnrolment(course.getEnrolledMembers().size() + 1)
                .dayOfWeek(course.getDayOfWeek())
                .courseDuration(formattedDuration)
                .facilityName(course.getFacility().getName())
                .instructorId(course.getInstructor().getUserId())
                .instructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName())
                .build();
    }
}