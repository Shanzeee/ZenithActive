package com.brvsk.ZenithActive.enrollment;

import com.brvsk.ZenithActive.session.SessionResponse;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import jakarta.transaction.Transactional;

import java.util.Set;
import java.util.UUID;

public interface SessionEnrollmentService {
    @Transactional
    void enrollMemberToSession(UUID sessionId, UUID memberId);
    Set<SessionResponse> getSessionsForMember(UUID userId);
    Set<MemberResponse> getMembersForSession(UUID sessionId);
}
