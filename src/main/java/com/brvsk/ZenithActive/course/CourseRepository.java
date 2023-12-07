package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Courses c " +
            "WHERE c.dayOfWeek = :dayOfWeek " +
            "AND :endTime > c.startTime AND :startTime < c.endTime " +
            "AND :facility = c.facility")
    List<Course> findOverlappingCourses(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("facility") Facility facility
    );

    @Query("SELECT c FROM Courses c " +
            "WHERE c.dayOfWeek = :dayOfWeek " +
            "AND :endTime > c.startTime AND :startTime < c.endTime " +
            "AND :instructor = c.instructor")
    List<Course> findOverlappingInstructorCourses(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("instructor") Instructor instructor
    );

    List<Course> getCoursesByCourseType(CourseType courseType);

    List<Course> getCoursesByInstructor_UserId(UUID instructorId);
}
