package com.brvsk.ZenithActive.review.instructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewInstructorRepository extends JpaRepository<ReviewInstructor, UUID> {

    Page<ReviewInstructor> findReviewInstructorByInstructor_UserId(UUID instructorId, Pageable page);
    List<ReviewInstructor> findReviewInstructorByInstructor_UserId(UUID instructorId);


}

