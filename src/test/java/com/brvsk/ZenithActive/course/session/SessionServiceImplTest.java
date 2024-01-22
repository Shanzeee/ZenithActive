package com.brvsk.ZenithActive.course.session;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.membership.MembershipType;
import com.brvsk.ZenithActive.membership.NoMembershipException;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberMapper;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionServiceImplTest {

    @InjectMocks
    private SessionServiceImpl sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private FacilityRepository facilityRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private EmailSender emailSender;

    @Test
    void createNewSession_Valid() {
        // Given
        SessionCreateRequest request = createSessionCreateRequest();

        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.of(new Course()));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(new Facility()));
        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(new Instructor()));
        when(sessionRepository.hasOverlappingSessionsWithFacility(any(), any(), any(), any())).thenReturn(false);
        when(sessionRepository.hasOverlappingSessionsWithInstructor(any(), any(), any(), any())).thenReturn(false);

        // When
        sessionService.createNewSession(request);

        // Then
        verify(sessionRepository, times(1)).save(Mockito.any());
    }

    @Test
    void createNewSession_OverlappingSessionsWithFacility() {
        // Given
        SessionCreateRequest request = createSessionCreateRequest();

        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.of(new Course()));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(new Facility()));
        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(new Instructor()));
        when(sessionRepository.hasOverlappingSessionsWithFacility(any(), any(), any(), any())).thenReturn(true);
        when(sessionRepository.hasOverlappingSessionsWithInstructor(any(), any(), any(), any())).thenReturn(false);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> sessionService.createNewSession(request));
        verify(sessionRepository, never()).save(Mockito.any());
    }

    @Test
    void createNewSession_OverlappingSessionsWithInstructor() {
        // Given
        SessionCreateRequest request = createSessionCreateRequest();

        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.of(new Course()));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(new Facility()));
        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(new Instructor()));
        when(sessionRepository.hasOverlappingSessionsWithFacility(any(), any(), any(), any())).thenReturn(false);
        when(sessionRepository.hasOverlappingSessionsWithInstructor(any(), any(), any(), any())).thenReturn(true);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> sessionService.createNewSession(request));
        verify(sessionRepository, never()).save(Mockito.any());
    }

    @Test
    void enrollMemberToSession_Valid() {
        // Given
        UUID sessionId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Facility facility = new Facility();
        facility.setFacilityType(FacilityType.POOL);
        Session session = new Session();
        session.setLocalDate(LocalDate.now().plusDays(1));
        session.setFacility(facility);
        session.setGroupSize(10);
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.FULL);
        membership.setEndDate(LocalDate.now().plusDays(10));
        Member member = new Member();
        member.setMembership(membership);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When
        sessionService.enrollMemberToSession(sessionId, memberId);

        // Then
        verify(sessionRepository, times(1)).save(Mockito.any());
        verify(memberRepository, times(1)).save(Mockito.any());
        verify(emailSender, times(1)).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
    }

    @Test
    void enrollMemberToSession_NoMembership() {
        // Given
        UUID sessionId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Session session = new Session();
        Member member = new Member();
        member.setMembership(null);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When, Then
        assertThrows(NoMembershipException.class, () -> sessionService.enrollMemberToSession(sessionId, memberId));
        verify(sessionRepository, never()).save(Mockito.any());
        verify(memberRepository, never()).save(Mockito.any());
        verify(emailSender, never()).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
    }

    @Test
    void enrollMemberToSession_InvalidMembershipForFacilityType() {
        // Given
        UUID sessionId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Facility facility = new Facility();
        facility.setFacilityType(FacilityType.POOL);
        Session session = new Session();
        session.setLocalDate(LocalDate.now().plusDays(1));
        session.setFacility(facility);
        session.setGroupSize(10);
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.GYM);
        membership.setEndDate(LocalDate.now().plusDays(10));
        Member member = new Member();
        member.setMembership(membership);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When, Then
        assertThrows(RuntimeException.class, () -> sessionService.enrollMemberToSession(sessionId, memberId));
        verify(sessionRepository, never()).save(Mockito.any());
        verify(memberRepository, never()).save(Mockito.any());
        verify(emailSender, never()).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
    }

    @Test
    void enrollMemberToSession_ExpiredMembership() {
        // Given
        UUID sessionId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Session session = new Session();
        Member member = new Member();
        member.setMembership(createMembership(LocalDate.now().minusDays(1)));

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(member));

        // When, Then
        assertThrows(RuntimeException.class, () -> sessionService.enrollMemberToSession(sessionId, memberId));
        verify(sessionRepository, never()).save(Mockito.any());
        verify(memberRepository, never()).save(Mockito.any());
        verify(emailSender, never()).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
    }

    @Test
    void getAllSessions_Valid() {
        // Given
        UUID session1Id = UUID.randomUUID();
        Session session1 = createSession(session1Id);

        UUID session2Id = UUID.randomUUID();
        Session session2 = createSession(session2Id);

        List<Session> sessions = Arrays.asList(session1, session2);

        when(sessionRepository.findAll()).thenReturn(sessions);
        when(sessionMapper.mapToResponse(session1)).thenReturn(createSessionResponse(session1));
        when(sessionMapper.mapToResponse(session2)).thenReturn(createSessionResponse(session2));

        // When
        List<SessionResponse> result = sessionService.getAllSessions();

        // Then
        assertEquals(2, result.size());
        verify(sessionMapper, times(2)).mapToResponse(Mockito.any());
    }

    @Test
    void getSessionsForCourseType_Valid() {
        // Given
        CourseType courseType = CourseType.ABT;

        UUID session1Id = UUID.randomUUID();
        Session session1 = createSession(session1Id);

        UUID session2Id = UUID.randomUUID();
        Session session2 = createSession(session2Id);

        List<Session> sessions = Arrays.asList(session1, session2);

        when(sessionRepository.findByCourse_CourseType(courseType)).thenReturn(sessions);
        when(sessionMapper.mapToResponse(session1)).thenReturn(createSessionResponse(session1));
        when(sessionMapper.mapToResponse(session2)).thenReturn(createSessionResponse(session2));

        // When
        List<SessionResponse> result = sessionService.getSessionsForCourseType(courseType);

        // Then
        assertEquals(2, result.size());
        verify(sessionMapper, times(2)).mapToResponse(Mockito.any());
    }

    @Test
    void getSessionsForInstructor_Valid() {
        // Given
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        instructor.setUserId(instructorId);

        UUID session1Id = UUID.randomUUID();
        Session session1 = createSession(session1Id);
        session1.setInstructor(instructor);

        UUID session2Id = UUID.randomUUID();
        Session session2 = createSession(session2Id);
        session2.setInstructor(instructor);

        List<Session> sessions = Arrays.asList(session1, session2);

        when(sessionRepository.findSessionByInstructor_UserId(instructorId)).thenReturn(sessions);
        when(sessionMapper.mapToResponse(session1)).thenReturn(createSessionResponse(session1));
        when(sessionMapper.mapToResponse(session2)).thenReturn(createSessionResponse(session2));

        // When
        List<SessionResponse> result = sessionService.getSessionsForInstructor(instructorId);

        // Then
        assertEquals(2, result.size());
        verify(sessionMapper, times(2)).mapToResponse(Mockito.any());
    }

    @Test
    void getSessionsForMember_Valid() {
        // Given
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setUserId(memberId);

        UUID session1Id = UUID.randomUUID();
        Session session1 = createSession(session1Id);
        Set<Session> enrolledSessions = new HashSet<>(Collections.singletonList(session1));
        member.setEnrolledSessions(enrolledSessions);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(sessionMapper.mapToResponse(session1)).thenReturn(createSessionResponse(session1));
        when(sessionRepository.findById(session1Id)).thenReturn(Optional.of(session1));

        // When
        Set<SessionResponse> result = sessionService.getSessionsForMember(memberId);

        // Then
        assertEquals(1, result.size());
        verify(sessionMapper, times(1)).mapToResponse(Mockito.any());
    }



    private SessionCreateRequest createSessionCreateRequest() {
        return SessionCreateRequest.builder()
                .courseId(UUID.randomUUID())
                .facilityId(UUID.randomUUID())
                .instructorId(UUID.randomUUID())
                .localDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .build();
    }

    private Membership createMembership(LocalDate endDate) {
        return Membership.builder()
                .id(UUID.randomUUID())
                .membershipType(MembershipType.FULL)
                .endDate(endDate)
                .build();
    }

    private Session createSession(UUID sessionId) {
        return Session.builder()
                .id(sessionId)
                .groupSize(10)
                .localDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();
    }

    private SessionResponse createSessionResponse(Session session) {
        return SessionResponse.builder()
                .sessionId(session.getId())
                .groupSize(session.getGroupSize())
                .localDate(session.getLocalDate())
                .build();
    }


}