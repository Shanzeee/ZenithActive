package com.brvsk.ZenithActive.review.instructor;

import com.brvsk.ZenithActive.excpetion.ReviewAlreadyExistsException;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsCreateRequest;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsType;
import com.brvsk.ZenithActive.review.*;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
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
public class ReviewInstructorServiceImpl implements ReviewService {

    private final ReviewInstructorRepository reviewInstructorRepository;
    private final InstructorRepository instructorRepository;
    private final MemberRepository memberRepository;
    private final ReviewMapper reviewMapper;
    private final LoyaltyPointsService loyaltyPointsService;
    @Override
    @Transactional
    public void createNewReview(ReviewCreateRequest request) {
        Member member = getMemberById(request.getMemberId());
        Instructor instructor = getInstructorById(request.getReviewedEntityId());

        checkIfUserAlreadyReviewedInstructor(member, instructor);

        ReviewInstructor reviewInstructor = toEntity(request);
        reviewInstructor.setMember(member);
        reviewInstructor.setInstructor(instructor);

        reviewInstructorRepository.save(reviewInstructor);


        LoyaltyPointsCreateRequest loyaltyPointsCreateRequest = buildLoyaltyPointsCreateRequest(request.getMemberId());
        loyaltyPointsService.addLoyaltyPoints(loyaltyPointsCreateRequest);
    }

    @Override
    public Page<ReviewResponse> getReviews(UUID instructorId, Pageable pageable) {
        Page<ReviewInstructor> reviews = reviewInstructorRepository.findReviewInstructorByInstructor_UserId(instructorId, pageable);

        List<ReviewResponse> reviewResponseList = reviews.getContent().stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewResponseList, reviews.getPageable(), reviews.getTotalElements());
    }

    @Override
    public Double getAverageRating(UUID instructorId) {
        List<ReviewInstructor> reviews = reviewInstructorRepository.findReviewInstructorByInstructor_UserId(instructorId);

        if (reviews.isEmpty()) {
            return null;
        }

        double sum = 0.0;
        for (ReviewInstructor review : reviews) {
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

    private Instructor getInstructorById(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new UserNotFoundException(instructorId));
    }

    private void checkIfUserAlreadyReviewedInstructor(Member member, Instructor instructor) {
        if (reviewInstructorRepository.existsByMemberAndInstructor(member, instructor)) {
            throw new ReviewAlreadyExistsException(instructor.getUserId());
        }
    }

    private ReviewInstructor toEntity(ReviewCreateRequest request) {
        return ReviewInstructor.builder()
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
