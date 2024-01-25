package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.user.member.MemberResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SessionService {

    void createNewSession(SessionCreateRequest request);
    void enrollMemberToSession(UUID sessionId, UUID memberId);
    List<SessionResponse> getAllSessions();
    List<SessionResponse> getSessionsForCourseType(CourseType courseType);
    List<SessionResponse> getSessionsForInstructor(UUID instructorId);
    Set<SessionResponse> getSessionsForMember(UUID userId);
    Set<MemberResponse> getMembersForSession(UUID courseId);
}
