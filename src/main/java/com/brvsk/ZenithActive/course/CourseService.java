package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.user.member.MemberResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CourseService {
    void createNewCourse(CourseCreateRequest request);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesForCourseType(CourseType courseType);
    List<CourseResponse> getCoursesForInstructor(UUID instructorId);
    Set<CourseResponse> getCoursesForMember(UUID userId);
    void enrolMemberToCourse(UUID courseId, UUID userId);
    Set<MemberResponse> getMembersForCourse(UUID courseId);
}
