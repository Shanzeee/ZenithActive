package com.brvsk.ZenithActive.member;

import com.brvsk.ZenithActive.course.CourseResponse;

import java.util.Set;
import java.util.UUID;

public interface MemberService {
    void createMember(MemberCreateRequest request);

    Set<CourseResponse> getCoursesForMember(UUID userId);
}
