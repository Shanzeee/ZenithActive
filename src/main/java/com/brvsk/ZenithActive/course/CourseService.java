package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.member.MemberResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CourseService {
    void createNewCourse(CourseCreateRequest request);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesForCourseType(CourseType courseType);
    List<CourseResponse> getCoursesForInstructor(UUID instructorId);
    @Transactional
    void enrolMemberToCourse(UUID courseId, UUID userId);
    Set<MemberResponse> getMembersForCourse(UUID courseId);
}
