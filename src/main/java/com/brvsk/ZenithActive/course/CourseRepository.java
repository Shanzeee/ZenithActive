package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.instructor.Instructor;
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
            "AND ((c.startTime BETWEEN :startTime AND :endTime) OR (c.endTime BETWEEN :startTime AND :endTime)) " +
            "AND c.facilities IN :facilities")
    List<Course> findOverlappingCourses(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("facilities") List<Facility> facilities);

    @Query("SELECT c FROM Courses c " +
            "WHERE c.dayOfWeek = :dayOfWeek " +
            "AND ((c.startTime BETWEEN :startTime AND :endTime) OR (c.endTime BETWEEN :startTime AND :endTime)) " +
            "AND c.instructor = :instructor")
    List<Course> findOverlappingInstructorCourses(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("instructor") Instructor instructor);
}
