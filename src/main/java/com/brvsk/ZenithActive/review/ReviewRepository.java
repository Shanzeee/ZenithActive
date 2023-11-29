package com.brvsk.ZenithActive.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT AVG(CAST(CAST(r.instructorRating AS int) AS float) + 1) FROM Review r WHERE r.instructor.id = :instructorId")
    Double getAverageInstructorRating(@Param("instructorId") UUID instructorId);

    @Query("SELECT AVG(CAST(CAST(r.courseRating AS int) AS float) + 1) FROM Review r WHERE r.course.id = :courseId")
    Double getAverageCourseRating(@Param("courseId") UUID courseId);
    Page<Review> findByCourseId(UUID courseId, Pageable pageable);
    Page<Review> findByInstructorUserId(UUID instructorId, Pageable pageable);
}
