package com.brvsk.ZenithActive.course.session;

import com.brvsk.ZenithActive.course.*;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityNotFoundException;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.membership.MembershipType;
import com.brvsk.ZenithActive.membership.NoMembershipException;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberMapper;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService{

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final FacilityRepository facilityRepository;
    private final InstructorRepository instructorRepository;
    private final MemberRepository memberRepository;
    private final SessionMapper sessionMapper;
    private final MemberMapper memberMapper;
    private final EmailSender emailSender;
    @Override
    @Transactional
    public void createNewSession(SessionCreateRequest request) {
        Course course = getCourse(request.getCourseId());
        Facility facility = getFacility(request.getFacilityId());
        Instructor instructor = getInstructor(request.getInstructorId());

        validateSessionHours(request.getLocalDate(), request.getStartTime(), request.getEndTime(), facility);
        validateInstructorAvailability(request.getLocalDate(),request.getStartTime(),request.getEndTime(), instructor);

        Session session = toDto(request);
        session.setCourse(course);
        session.setFacility(facility);
        session.setInstructor(instructor);

        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void enrollMemberToSession(UUID sessionId, UUID memberId) {
        Session session = getSession(sessionId);
        Member member = getMember(memberId);

        performEnrollmentChecks(member, session);
        checkIfMemberAlreadyEnrolled(session, member);
        checkIfSessionIsFull(session);

        session.getEnrolledMembers().add(member);
        member.getEnrolledSessions().add(session);

        sessionRepository.save(session);
        memberRepository.save(member);

        emailSender.sendEnrollmentConfirmation(member, session);
    }

    @Override
    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(sessionMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<SessionResponse> getSessionsForCourseType(CourseType courseType) {
        List<Session> sessions = sessionRepository.findByCourse_CourseType(courseType);

        return sessions.stream()
                .map(sessionMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionResponse> getSessionsForInstructor(UUID instructorId) {
        return sessionRepository.findSessionByInstructor_UserId(instructorId)
                .stream()
                .map(sessionMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Set<SessionResponse> getSessionsForMember(UUID userId) {
        Member member = getMember(userId);

        return member.getEnrolledSessions()
                .stream()
                .map(sessionMapper::mapToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MemberResponse> getMembersForSession(UUID sessionId) {
        Session session = getSession(sessionId);

        return session.getEnrolledMembers()
                .stream()
                .map(memberMapper::toMemberResponse)
                .collect(Collectors.toSet());
    }

    private void validateSessionHours(LocalDate localDate, LocalTime startTime, LocalTime endTime, Facility facility) {
        boolean hasOverlappingSessions = sessionRepository.hasOverlappingSessionsWithFacility(localDate, startTime, endTime, facility);

        if (hasOverlappingSessions) {
            throw new IllegalArgumentException("The session hours overlap with existing sessions.");
        }
    }

    private void validateInstructorAvailability(LocalDate localDate, LocalTime startTime, LocalTime endTime, Instructor instructor) {
        boolean hasOverlappingSessions = sessionRepository.hasOverlappingSessionsWithInstructor(localDate, startTime, endTime, instructor);

        if (hasOverlappingSessions) {
            throw new IllegalArgumentException("The instructor is not available during the specified hours.");
        }
    }

    private void performEnrollmentChecks(Member member, Session session) {
        checkIfMemberHasMembership(member);
        checkIfMembershipIsValidForFacilityType(member.getMembership().getMembershipType(), session.getFacility().getFacilityType());
        checkIfMembershipIsValidForMember(member, session);
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

    private boolean isMembershipValidForFacilityType(MembershipType memberMembershipType, FacilityType requiredFacilityType) {
        return switch (requiredFacilityType) {
            case POOL -> memberMembershipType == MembershipType.POOL || memberMembershipType == MembershipType.FULL;
            case GYM -> memberMembershipType == MembershipType.GYM || memberMembershipType == MembershipType.FULL;
        };
    }

    private void checkIfMembershipIsValidForMember(Member member, Session session) {
        LocalDate membershipEndDate = member.getMembership().getEndDate();
        LocalDate sessionDate = session.getLocalDate();

        if (sessionDate.isAfter(membershipEndDate)) {
            throw new RuntimeException("Member is trying to enroll in a session after membership expiration date.");
        }
    }

    private void checkIfMemberAlreadyEnrolled(Session session, Member member) {
        if (session.getEnrolledMembers().contains(member)) {
            throw new MemberAlreadyEnrolledException(member.getUserId(), session.getId());
        }
    }

    private void checkIfSessionIsFull(Session session) {
        if (session.getEnrolledMembers().size() >= session.getGroupSize()) {
            throw new SessionIsFullException(session.getId());
        }
    }

    private Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CourseNotFoundException(sessionId));
    }
    private Course getCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
    private Facility getFacility(UUID facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityNotFoundException(facilityId));
    }
    private Instructor getInstructor(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new UserNotFoundException(instructorId));
    }

    private Member getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private Session toDto (SessionCreateRequest request) {
        return Session.builder()
                .groupSize(request.getGroupSize())
                .localDate(request.getLocalDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
