package com.brvsk.ZenithActive.course;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    void createNewCourse(CourseCreateRequest request);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesForCourseType(CourseType courseType);
    List<CourseResponse> getCoursesForInstructor(UUID instructorId);
}
