package com.brvsk.ZenithActive.review.instructor;

import com.brvsk.ZenithActive.loyalty.LoyaltyPointsCreateRequest;
import com.brvsk.ZenithActive.loyalty.LoyaltyPointsService;
import com.brvsk.ZenithActive.review.Rating;
import com.brvsk.ZenithActive.review.ReviewCreateRequest;
import com.brvsk.ZenithActive.review.ReviewMapper;
import com.brvsk.ZenithActive.review.ReviewResponse;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewInstructorServiceImplTest {

    @Mock
    private ReviewInstructorRepository reviewInstructorRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private LoyaltyPointsService loyaltyPointsService;

    @InjectMocks
    private ReviewInstructorServiceImpl reviewInstructorService;

    @Test
    void createNewReview_Success() {
        // Given
        UUID memberId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(memberId, instructorId, Rating.STAR_5, "Great instructor!");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(new Instructor()));
        when(reviewInstructorRepository.existsByMemberAndInstructor(any(Member.class), any(Instructor.class))).thenReturn(false);
        // When
        reviewInstructorService.createNewReview(reviewCreateRequest);

        // Then
        verify(reviewInstructorRepository).save(any(ReviewInstructor.class));
        verify(loyaltyPointsService).addLoyaltyPoints(any(LoyaltyPointsCreateRequest.class));
    }

    @Test
    void getReviews_Success() {
        // Given
        UUID instructorId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        List<ReviewInstructor> reviewInstructors = List.of(
                new ReviewInstructor(),
                new ReviewInstructor()
        );
        Page<ReviewInstructor> reviewInstructorPage = new PageImpl<>(reviewInstructors, pageable, reviewInstructors.size());

        when(reviewInstructorRepository.findReviewInstructorByInstructor_UserId(eq(instructorId), eq(pageable)))
                .thenReturn(reviewInstructorPage);
        when(reviewMapper.toReviewResponse(any(ReviewInstructor.class))).thenAnswer(invocation -> new ReviewResponse());

        // When
        Page<ReviewResponse> result = reviewInstructorService.getReviews(instructorId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(reviewInstructors.size(), result.getContent().size());
        verify(reviewInstructorRepository).findReviewInstructorByInstructor_UserId(instructorId, pageable);
    }

    @Test
    void getAverageRating_Success() {
        // Given
        Member member = new Member();
        member.setUserId(UUID.randomUUID());

        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        instructor.setUserId(instructorId);

        List<ReviewInstructor> reviews = List.of(
                new ReviewInstructor(UUID.randomUUID(),member,Rating.STAR_5,"good", LocalDate.now(),instructor),
                new ReviewInstructor(UUID.randomUUID(),member,Rating.STAR_1,"good", LocalDate.now(),instructor)
        );

        when(reviewInstructorRepository.findReviewInstructorByInstructor_UserId(instructorId)).thenReturn(reviews);

        // When
        Double averageRating = reviewInstructorService.getAverageRating(instructorId);

        // Then
        assertNotNull(averageRating);
        assertEquals(3, averageRating);
    }
}