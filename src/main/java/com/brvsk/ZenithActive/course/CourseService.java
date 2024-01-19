package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.user.member.MemberResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CourseService {
    void createNewCourse(CourseCreateRequest request);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesForCourseType(CourseType courseType);
}
