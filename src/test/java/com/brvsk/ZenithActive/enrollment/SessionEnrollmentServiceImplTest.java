package com.brvsk.ZenithActive.enrollment;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.membership.MembershipRepository;
import com.brvsk.ZenithActive.membership.MembershipType;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.session.SessionMapper;
import com.brvsk.ZenithActive.session.SessionRepository;
import com.brvsk.ZenithActive.session.SessionResponse;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionEnrollmentServiceImplTest {

    @InjectMocks
    private SessionEnrollmentServiceImpl sessionEnrollmentService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private EmailSender emailSender;
    @Mock
    private MembershipRepository membershipRepository;

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
        member.getMemberships().add(membership);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(membershipRepository.findByMember_UserIdAndStartDateBeforeAndEndDateAfter(any(),any(),any())).thenReturn(List.of(membership));

        // When
        sessionEnrollmentService.enrollMemberToSession(sessionId, memberId);

        // Then
        verify(sessionRepository, times(1)).save(Mockito.any());
        verify(memberRepository, times(1)).save(Mockito.any());
        verify(emailSender, times(1)).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
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
        member.getMemberships().add(membership);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When, Then
        assertThrows(RuntimeException.class, () -> sessionEnrollmentService.enrollMemberToSession(sessionId, memberId));
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
        member.getMemberships().add(createMembership(LocalDate.now().minusDays(1)));

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(session));
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(member));

        // When, Then
        assertThrows(RuntimeException.class, () -> sessionEnrollmentService.enrollMemberToSession(sessionId, memberId));
        verify(sessionRepository, never()).save(Mockito.any());
        verify(memberRepository, never()).save(Mockito.any());
        verify(emailSender, never()).sendEnrollmentConfirmation(Mockito.any(), Mockito.any());
    }

    @Test
    void getSessionsForMember_Valid() {
        // Given
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setUserId(memberId);

        UUID sessionId = UUID.randomUUID();
        Session session = createSession(sessionId);
        SessionEnrollment enrollment = new SessionEnrollment();
        enrollment.setSession(session);
        enrollment.setMember(member);
        Set<SessionEnrollment> enrollments = new HashSet<>(Collections.singletonList(enrollment));
        member.setEnrollments(enrollments);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(sessionMapper.mapToResponse(any(Session.class))).thenReturn(createSessionResponse(session));

        // When
        Set<SessionResponse> result = sessionEnrollmentService.getSessionsForMember(memberId);

        // Then
        assertEquals(1, result.size());
        verify(sessionMapper, times(1)).mapToResponse(any(Session.class));
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