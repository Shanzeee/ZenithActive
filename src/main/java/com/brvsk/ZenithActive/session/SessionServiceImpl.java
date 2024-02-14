package com.brvsk.ZenithActive.session;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.excpetion.CourseNotFoundException;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.excpetion.FacilityNotFoundException;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService{

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final FacilityRepository facilityRepository;
    private final InstructorRepository instructorRepository;
    private final SessionMapper sessionMapper;
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

    private Session toDto (SessionCreateRequest request) {
        return Session.builder()
                .groupSize(request.getGroupSize())
                .localDate(request.getLocalDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
