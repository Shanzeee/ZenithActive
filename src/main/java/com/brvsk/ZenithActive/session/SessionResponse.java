package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.CourseType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
public class SessionResponse {
    private UUID courseId;
    private CourseType courseType;
    private String courseName;
    private String courseDescription;
    private UUID sessionId;
    private String facilityName;
    private UUID instructorId;
    private String instructorName;
    private Integer groupSize;
    private LocalDate localDate;
    private String courseDuration;
}
