package com.brvsk.ZenithActive.review.course;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.user.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewCourseRepository extends JpaRepository<ReviewCourse, UUID> {

    Page<ReviewCourse> findReviewCoursesByCourse_Id(UUID courseId, Pageable pageable);
    List<ReviewCourse> findReviewCoursesByCourse_Id(UUID courseId);
    boolean existsByMemberAndCourse (Member member, Course course);

}
