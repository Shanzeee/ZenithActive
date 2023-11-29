package com.brvsk.ZenithActive.review;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseNotFoundException;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberRepository;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final InstructorRepository instructorRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public void createNewReview(ReviewCreateRequest request){
        Member member = getMemberById(request.getMemberId());
        Course course = getCourseById(request.getCourseId());
        Instructor instructor = course.getInstructor();

        Review review = toEntity(request);
        review.setMember(member);
        review.setInstructor(instructor);
        review.setCourse(course);

        member.getReviews().add(review);
        instructor.getReviews().add(review);
        course.getReviews().add(review);

        reviewRepository.save(review);
        memberRepository.save(member);
        instructorRepository.save(instructor);
        courseRepository.save(course);
    }

    private Member getMemberById(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private Course getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    private Review toEntity(ReviewCreateRequest request){
        return Review.builder()
                .id(UUID.randomUUID())
                .instructorRating(request.getInstructorRating())
                .instructorComment(request.getInstructorComment())
                .courseRating(request.getCourseRating())
                .courseComment(request.getCourseComment())
                .build();
    }
}
