package com.brvsk.ZenithActive.review.course;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.excpetion.CourseNotFoundException;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsCreateRequest;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsType;
import com.brvsk.ZenithActive.review.*;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewCourseServiceImpl implements ReviewService {

    private final ReviewCourseRepository reviewCourseRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final ReviewMapper reviewMapper;
    private final LoyaltyPointsService loyaltyPointsService;
    @Override
    @Transactional
    public void createNewReview(ReviewCreateRequest request) {
        Member member = getMemberById(request.getMemberId());
        Course course = getCourseById(request.getReviewedEntityId());

        checkIfUserAlreadyReviewedCourse(member, course);

        ReviewCourse reviewCourse = toEntity(request);
        reviewCourse.setMember(member);
        reviewCourse.setCourse(course);

        reviewCourseRepository.save(reviewCourse);


        LoyaltyPointsCreateRequest loyaltyPointsCreateRequest = buildLoyaltyPointsCreateRequest(request.getMemberId());
        loyaltyPointsService.addLoyaltyPoints(loyaltyPointsCreateRequest);
    }

    @Override
    public Page<ReviewResponse> getReviews(UUID courseId, Pageable pageable) {
        Page<ReviewCourse> reviews = reviewCourseRepository.findReviewCoursesByCourse_Id(courseId, pageable);

        List<ReviewResponse> reviewResponseList = reviews.getContent().stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewResponseList, reviews.getPageable(), reviews.getTotalElements());
    }

    @Override
    public Double getAverageRating(UUID courseId) {
        List<ReviewCourse> reviews = reviewCourseRepository.findReviewCoursesByCourse_Id(courseId);

        if (reviews.isEmpty()) {
            return null;
        }

        double sum = 0.0;
        for (ReviewCourse review : reviews) {
            sum += review.getRating().getNumericValue();
        }

        double average = sum / reviews.size();

        average = Math.round(average * 10.0) / 10.0;

        return average;
    }

    private Member getMemberById(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private Course getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    private void checkIfUserAlreadyReviewedCourse(Member member, Course course) {
        if (reviewCourseRepository.existsByMemberAndCourse(member, course)) {
            throw new ReviewAlreadyExistsException(course.getId());
        }
    }

    private ReviewCourse toEntity(ReviewCreateRequest request) {
        return ReviewCourse.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
    }

    private LoyaltyPointsCreateRequest buildLoyaltyPointsCreateRequest(UUID memberId) {
        return LoyaltyPointsCreateRequest
                .builder()
                .loyaltyPointsType(LoyaltyPointsType.REVIEW)
                .pointsAmount(10)
                .memberId(memberId)
                .build();
    }
}
