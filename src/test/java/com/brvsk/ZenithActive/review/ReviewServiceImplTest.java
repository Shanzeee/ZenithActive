package com.brvsk.ZenithActive.review;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseNotFoundException;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.instructor.Speciality;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewReview_ValidRequest_SuccessfullyCreated() {
        // Given
        UUID memberId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();

        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .memberId(memberId)
                .courseId(courseId)
                .courseRating(Rating.STAR_4)
                .courseComment("Good course")
                .instructorRating(Rating.STAR_5)
                .instructorComment("Excellent instructor")
                .build();

        Member member = createMember(memberId);
        Course course = createCourse(courseId);
        Instructor instructor = createInstructor(instructorId);

        course.setInstructor(instructor);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        assertDoesNotThrow(() -> reviewService.createNewReview(request));

        // Then
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(memberRepository, times(1)).save(member);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void createNewReview_InvalidMember_ThrowsException() {
        // Given
        UUID memberId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        ReviewCreateRequest request = createValidReviewCreateRequest(memberId, courseId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> reviewService.createNewReview(request));
        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    void createNewReview_InvalidCourse_ThrowsException() {
        // Given
        UUID memberId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        ReviewCreateRequest request = createValidReviewCreateRequest(memberId, courseId);

        Member member = createMember(memberId);
        Instructor instructor = createInstructor(instructorId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CourseNotFoundException.class, () -> reviewService.createNewReview(request));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    private ReviewCreateRequest createValidReviewCreateRequest(UUID memberId, UUID courseId) {
        return ReviewCreateRequest.builder()
                .memberId(memberId)
                .courseId(courseId)
                .instructorRating(Rating.STAR_5)
                .instructorComment("Excellent instructor!")
                .courseRating(Rating.STAR_4)
                .courseComment("Good course!")
                .build();
    }

    private Member createMember(UUID memberId) {
        Member member = new Member();
        member.setUserId(memberId);
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setGender(Gender.MALE);
        member.setHeight(180);
        member.setWeight(75);
        return member;
    }

    private Instructor createInstructor(UUID instructorId) {
        Instructor instructor = new Instructor();
        instructor.setUserId(instructorId);
        instructor.setFirstName("InstructorFirstName");
        instructor.setLastName("InstructorLastName");
        instructor.setGender(Gender.FEMALE);
        instructor.setDescription("Experienced instructor");
        instructor.setSpecialities(Arrays.asList(Speciality.YOGA, Speciality.PILATES));
        return instructor;
    }

    private Course createCourse(UUID courseId) {
        Course course = new Course();
        course.setId(courseId);
        course.setCourseType(CourseType.STRETCH);
        course.setName("CourseName");
        course.setDescription("CourseDescription");
        course.setGroupSize(15);
        course.setDayOfWeek(DayOfWeek.MONDAY);
        course.setStartTime(LocalTime.of(10, 0));
        course.setEndTime(LocalTime.of(12, 0));
        return course;
    }
}