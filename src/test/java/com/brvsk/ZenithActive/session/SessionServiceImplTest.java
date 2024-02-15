package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private SessionMapper sessionMapper;

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