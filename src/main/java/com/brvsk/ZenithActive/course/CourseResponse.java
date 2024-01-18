package com.brvsk.ZenithActive.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class CourseResponse {
    private UUID courseId;
    private CourseType courseType;
    private String courseName;
    private String courseDescription;
    private Integer groupSize;
    private Integer currentEnrolment;
    private LocalDate localDate;
    private String courseDuration;
    private String facilityName;
    private UUID instructorId;
    private String instructorName;
}
