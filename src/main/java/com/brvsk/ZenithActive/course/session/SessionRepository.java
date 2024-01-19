package com.brvsk.ZenithActive.course.session;

import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Query("SELECT COUNT(s) > 0 FROM Session s " +
            "WHERE :localDate = s.localDate " +
            "AND :endTime > s.startTime AND :startTime < s.endTime " +
            "AND :facility = s.facility")
    boolean hasOverlappingSessionsWithFacility(
            @Param("localDate") LocalDate localDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("facility") Facility facility
    );

    @Query("SELECT COUNT(s) > 0 FROM Session s " +
            "WHERE :localDate = s.localDate " +
            "AND :endTime > s.startTime AND :startTime < s.endTime " +
            "AND :instructor = s.instructor")
    boolean hasOverlappingSessionsWithInstructor(
            @Param("localDate") LocalDate localDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("instructor") Instructor instructor
    );

    List<Session> findByCourse_CourseType(CourseType courseType);
    List<Session> findSessionByInstructor_UserId(UUID instructorId);
}
