package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.CourseType;

import java.util.List;
import java.util.UUID;

public interface SessionService {

    void createNewSession(SessionCreateRequest request);
    List<SessionResponse> getAllSessions();
    List<SessionResponse> getSessionsForCourseType(CourseType courseType);
    List<SessionResponse> getSessionsForInstructor(UUID instructorId);
}
