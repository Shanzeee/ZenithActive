package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.instructor.Speciality;
import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberMapper;
import com.brvsk.ZenithActive.member.MemberRepository;
import com.brvsk.ZenithActive.member.MemberResponse;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.Gender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseServiceImplTest {

    private CourseServiceImpl courseServiceImpl;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private FacilityRepository facilityRepository;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private EmailSender emailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseServiceImpl = new CourseServiceImpl(courseRepository, instructorRepository, facilityRepository, courseMapper, memberRepository, memberMapper, emailSender) {
        };
    }

    @Test
    void createNewCourse_ValidCourse_NoOverlappingCourses_NoInstructorAvailabilityProblem() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        Facility facility = createFacility();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(instructor));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(facility));
        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Facility.class)))
                .thenReturn(List.of());

        when(courseRepository.findOverlappingInstructorCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Instructor.class)))
                .thenReturn(List.of());

        // When
        assertDoesNotThrow(() -> courseServiceImpl.createNewCourse(request));

        // Then
        verify(courseRepository, times(1)).save(ArgumentMatchers.any(Course.class));
    }

    @Test
    void createNewCourse_OverlappingCourses_ThrowsException() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        Facility facility = createFacility();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(instructor));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(facility));

        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Facility.class)))
                .thenReturn(Collections.singletonList(createNotValidCourse()));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.createNewCourse(request));

        verify(courseRepository, times(1)).findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Facility.class));
    }

    @Test
    void createNewCourse_InstructorNotAvailable_ThrowsException() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        Facility facility = createFacility();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(Optional.of(instructor));
        when(facilityRepository.findById(request.getFacilityId())).thenReturn(Optional.of(facility));

        when(courseRepository.findOverlappingInstructorCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Instructor.class)))
                .thenReturn(Collections.singletonList(createNotValidCourse()));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.createNewCourse(request));

        verify(courseRepository, times(1)).findOverlappingInstructorCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Instructor.class));
    }

    @Test
    void getAllCourses_NoCourses_ReturnsEmptyList() {
        // Given
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CourseResponse> result = courseServiceImpl.getAllCourses();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getAllCourses_CoursesExist_ReturnsMappedCourseResponses() {
        // Given
        Course course1 = createCourse("Course 1");
        Course course2 = createCourse("Course 2");
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.mapToResponse(course1)).thenReturn(createCourseResponse(course1));
        when(courseMapper.mapToResponse(course2)).thenReturn(createCourseResponse(course2));

        // When
        List<CourseResponse> result = courseServiceImpl.getAllCourses();

        // Then
        assertThat(result).hasSize(2);

        CourseResponse expectedResponse1 = createCourseResponse(course1);
        CourseResponse expectedResponse2 = createCourseResponse(course2);

        assertThat(result)
                .usingElementComparator(Comparator.comparing(CourseResponse::getCourseName))
                .containsExactlyInAnyOrder(expectedResponse1, expectedResponse2);
    }

    @Test
    void getCoursesForCourseType_NoCourses_ReturnsEmptyList() {
        // Given
        CourseType courseType = CourseType.ACTIVITY;
        when(courseRepository.getCoursesByCourseType(courseType)).thenReturn(Collections.emptyList());

        // When
        List<CourseResponse> result = courseServiceImpl.getCoursesForCourseType(courseType);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getCoursesForCourseType_CoursesExist_ReturnsMappedCourseResponses() {
        // Given
        CourseType courseType = CourseType.ACTIVITY;
        Course course1 = createCourse("Yoga Course 1", courseType);
        Course course2 = createCourse("Yoga Course 2", courseType);
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.getCoursesByCourseType(courseType)).thenReturn(courses);
        when(courseMapper.mapToResponse(course1)).thenReturn(createCourseResponse(course1));
        when(courseMapper.mapToResponse(course2)).thenReturn(createCourseResponse(course2));

        // When
        List<CourseResponse> result = courseServiceImpl.getCoursesForCourseType(courseType);

        // Then
        assertThat(result).hasSize(2);

        CourseResponse expectedResponse1 = createCourseResponse(course1);
        CourseResponse expectedResponse2 = createCourseResponse(course2);

        assertThat(result)
                .usingElementComparator(Comparator.comparing(CourseResponse::getCourseName))
                .containsExactlyInAnyOrder(expectedResponse1, expectedResponse2);
    }

    @Test
    void getCoursesForInstructor_NoCourses_ReturnsEmptyList() {
        // Given
        UUID instructorId = UUID.randomUUID();
        when(courseRepository.getCoursesByInstructor_UserId(instructorId)).thenReturn(Collections.emptyList());

        // When
        List<CourseResponse> result = courseServiceImpl.getCoursesForInstructor(instructorId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getCoursesForInstructor_CoursesExist_ReturnsMappedCourseResponses() {
        // Given
        UUID instructorId = UUID.randomUUID();
        Course course1 = createCourse("Instructor Course 1");
        Course course2 = createCourse("Instructor Course 2");
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.getCoursesByInstructor_UserId(instructorId)).thenReturn(courses);
        when(courseMapper.mapToResponse(course1)).thenReturn(createCourseResponse(course1));
        when(courseMapper.mapToResponse(course2)).thenReturn(createCourseResponse(course2));

        // When
        List<CourseResponse> result = courseServiceImpl.getCoursesForInstructor(instructorId);

        // Then
        assertThat(result).hasSize(2);

        CourseResponse expectedResponse1 = createCourseResponse(course1);
        CourseResponse expectedResponse2 = createCourseResponse(course2);

        assertThat(result)
                .usingElementComparator(Comparator.comparing(CourseResponse::getCourseName))
                .containsExactlyInAnyOrder(expectedResponse1, expectedResponse2);
    }

    @Test
    void enrolMemberToCourse_ValidCourseAndUser_SuccessfullyEnrolled() {
        // Given
        UUID courseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Course course = createCourse("Test Course");
        Member member = createMember("Johnny", "Doe", Gender.MALE);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));

        // When
        assertDoesNotThrow(() -> courseServiceImpl.enrolMemberToCourse(courseId, userId));

        // Then
        verify(courseRepository, times(1)).save(course);
        verify(memberRepository, times(1)).save(member);
        Assertions.assertTrue(course.getEnrolledMembers().contains(member));
        Assertions.assertTrue(member.getEnrolledCourses().contains(course));
    }

    @Test
    void getMembersForCourse_CourseWithEnrolledMembers_ReturnsMemberResponses() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = createCourse("Test Course");
        Member member1 = createMember("John", "Dog", Gender.MALE);
        Member member2 = createMember("Jane", "Doe", Gender.FEMALE);

        course.getEnrolledMembers().addAll(Arrays.asList(member1, member2));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(memberMapper.toMemberResponse(member1)).thenReturn(createMemberResponse(member1));
        when(memberMapper.toMemberResponse(member2)).thenReturn(createMemberResponse(member2));

        // When
        Set<MemberResponse> result = courseServiceImpl.getMembersForCourse(courseId);

        // Then
        assertThat(result).hasSize(2);

        MemberResponse expectedResponse1 = createMemberResponse(member1);
        MemberResponse expectedResponse2 = createMemberResponse(member2);

        assertThat(result)
                .usingElementComparator(Comparator.comparing(MemberResponse::getFirstName))
                .containsExactlyInAnyOrder(expectedResponse1, expectedResponse2);
    }



    private CourseCreateRequest createValidCourseCreateRequest() {
        return CourseCreateRequest
                .builder()
                .courseType(CourseType.STRETCH)
                .name("Test course")
                .description("Description of test course")
                .groupSize(15)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(12, 0, 0))
                .facilityId(UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff91")) //Valid facility ID
                .instructorId(UUID.fromString("eb5876df-bc26-4232-8779-172c1b341700")) //Valid instructor ID
                .build();
    }
    private Course createNotValidCourse() {
        return Course.builder()
                .id(UUID.randomUUID())
                .courseType(CourseType.STRETCH)
                .name("Existing Course")
                .description("Description of Existing Course")
                .groupSize(20)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(11, 0, 0))
                .endTime(LocalTime.of(13, 0, 0))
                .facility(createFacility())
                .instructor(createInstructor())
                .build();
    }

    private Instructor createInstructor() {
        Instructor instructor = new Instructor();
        instructor.setUserId(UUID.fromString("eb5876df-bc26-4232-8779-172c1b341700"));
        instructor.setFirstName("Kacper");
        instructor.setLastName("Doe");
        instructor.setGender(Gender.MALE);
        instructor.setDescription("Some description");
        instructor.setSpecialities(Collections.singletonList(Speciality.YOGA));
        return instructor;
    }

    private Facility createFacility() {
        Map<DayOfWeek, LocalTime> openingHoursStart = new HashMap<>();
        Map<DayOfWeek, LocalTime> openingHoursEnd = new HashMap<>();

        LocalTime openingTime = LocalTime.of(6, 30);
        LocalTime closingTimeWeekdays = LocalTime.of(21, 30);
        LocalTime closingTimeSunday = LocalTime.of(20, 30);

        for (DayOfWeek day : DayOfWeek.values()) {
            openingHoursStart.put(day, openingTime);
            openingHoursEnd.put(day, (day == DayOfWeek.SUNDAY) ? closingTimeSunday : closingTimeWeekdays);
        }

        return Facility.builder()
                .id(UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff91"))
                .facilityType(FacilityType.POOL)
                .name("Pool Line 1")
                .description("Line 2 description")
                .openingHoursStart(openingHoursStart)
                .openingHoursEnd(openingHoursEnd)
                .build();
    }

    private Course createCourse(String name) {
        return Course.builder()
                .id(UUID.randomUUID())
                .courseType(CourseType.ACTIVITY)
                .name(name)
                .description("Course description")
                .groupSize(15)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(12, 0, 0))
                .enrolledMembers(new HashSet<>())
                .build();
    }

    private Course createCourse(String name, CourseType courseType) {
        return Course.builder()
                .id(UUID.randomUUID())
                .courseType(courseType)
                .name(name)
                .description("Course description")
                .groupSize(15)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(12, 0, 0))
                .build();
    }

    private CourseResponse createCourseResponse(Course course) {
        return CourseResponse.builder()
                .courseId(course.getId())
                .courseType(course.getCourseType())
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .groupSize(course.getGroupSize())
                .dayOfWeek(course.getDayOfWeek())
                .courseDuration("10:00 - 12:00")
                .facilityName("Facility")
                .instructorId(UUID.randomUUID())
                .instructorName("Instructor")
                .build();
    }

    private Member createMember(String firstName, String lastname, Gender gender){
        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastname);
        member.setGender(gender);
        return member;
    }

    private MemberResponse createMemberResponse(Member member){
        return MemberResponse
                .builder()
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .gender(member.getGender())
                .build();
    }
}