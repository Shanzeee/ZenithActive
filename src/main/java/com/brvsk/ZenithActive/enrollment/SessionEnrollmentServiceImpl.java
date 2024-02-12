package com.brvsk.ZenithActive.enrollment;

import com.brvsk.ZenithActive.course.CourseNotFoundException;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.membership.MembershipType;
import com.brvsk.ZenithActive.membership.NoMembershipException;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.session.*;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberMapper;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class SessionEnrollmentServiceImpl implements SessionEnrollmentService{

    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final SessionMapper sessionMapper;
    private final MemberMapper memberMapper;
    private final EmailSender emailSender;

    @Override
    @Transactional
    public void enrollMemberToSession(UUID sessionId, UUID memberId) {
        Session session = getSession(sessionId);
        Member member = getMember(memberId);

        performEnrollmentChecks(member, session);

        SessionEnrollment enrollment = new SessionEnrollment(new SessionEnrollmentId(memberId, sessionId), session, member);
        session.getEnrollments().add(enrollment);
        member.getEnrollments().add(enrollment);

        sessionRepository.save(session);
        memberRepository.save(member);

        emailSender.sendEnrollmentConfirmation(member, session);
    }
    @Override
    public Set<SessionResponse> getSessionsForMember(UUID userId) {
        Member member = getMember(userId);

        return member.getEnrollments().stream()
                .map(SessionEnrollment::getSession)
                .map(sessionMapper::mapToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MemberResponse> getMembersForSession(UUID sessionId) {
        Session session = getSession(sessionId);

        return session.getEnrollments().stream()
                .map(SessionEnrollment::getMember)
                .map(memberMapper::toMemberResponse)
                .collect(Collectors.toSet());
    }

    private void performEnrollmentChecks(Member member, Session session) {
        checkIfMemberAlreadyEnrolled(member, session);
        checkIfSessionIsFull(session);
        checkIfMemberHasMembership(member);
        checkIfMembershipIsValidForFacilityType(member.getMembership().getMembershipType(), session.getFacility().getFacilityType());
        checkIfMembershipIsValidForMember(member, session);
    }

    private void checkIfMemberAlreadyEnrolled(Member member, Session session) {
        boolean isAlreadyEnrolled = member.getEnrollments().stream()
                .anyMatch(enrollment -> enrollment.getSession().equals(session));
        if (isAlreadyEnrolled) {
            throw new MemberAlreadyEnrolledException(member.getUserId(), session.getId());
        }
    }

    private void checkIfSessionIsFull(Session session) {
        long enrolledCount = session.getEnrollments().size();
        if (enrolledCount >= session.getGroupSize()) {
            throw new SessionIsFullException(session.getId());
        }
    }

    private void checkIfMemberHasMembership(Member member) {
        if (member.getMembership() == null) {
            throw new NoMembershipException(member.getUserId());
        }
    }

    private void checkIfMembershipIsValidForFacilityType(MembershipType memberMembershipType, FacilityType requiredFacilityType) {
        if (!isMembershipValidForFacilityType(memberMembershipType, requiredFacilityType)) {
            throw new RuntimeException("Member does not have the required membership type for enrollment");
        }
    }

    private void checkIfMembershipIsValidForMember(Member member, Session session) {
        LocalDate sessionDate = session.getLocalDate();
        LocalDate membershipEndDate = member.getMembership().getEndDate();
        if (sessionDate.isAfter(membershipEndDate)) {
            throw new RuntimeException("Member is trying to enroll in a session after membership expiration date.");
        }
    }

    private boolean isMembershipValidForFacilityType(MembershipType memberMembershipType, FacilityType requiredFacilityType) {
        return switch (requiredFacilityType) {
            case POOL -> memberMembershipType == MembershipType.POOL || memberMembershipType == MembershipType.FULL;
            case GYM -> memberMembershipType == MembershipType.GYM || memberMembershipType == MembershipType.FULL;
        };
    }

    private Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CourseNotFoundException(sessionId));
    }

    private Member getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

}
