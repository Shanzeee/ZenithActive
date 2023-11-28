package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.instructor.Speciality;
import com.brvsk.ZenithActive.user.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseServiceImpl = new CourseServiceImpl(courseRepository, instructorRepository, facilityRepository) {
        };
    }

    @Test
    void createNewCourse_ValidCourse_NoOverlappingCourses_NoInstructorAvailabilityProblem() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        List<Facility> facilities = createFacilities();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(java.util.Optional.of(instructor));
        when(facilityRepository.findAllById(request.getFacilitiesId())).thenReturn(facilities);
        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.anyList()))
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
        List<Facility> facilities = createFacilities();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(java.util.Optional.of(instructor));
        when(facilityRepository.findAllById(request.getFacilitiesId())).thenReturn(facilities);
        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(createNotValidCourse()));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.createNewCourse(request));
    }

    @Test
    void createNewCourse_InstructorNotAvailable_ThrowsException() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        List<Facility> facilities = createFacilities();

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(java.util.Optional.of(instructor));
        when(facilityRepository.findAllById(request.getFacilitiesId())).thenReturn(facilities);
        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.anyList()))
                .thenReturn(List.of());

        when(courseRepository.findOverlappingInstructorCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Instructor.class)))
                .thenReturn(Collections.singletonList(createNotValidCourse()));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.createNewCourse(request));
    }

    @Test
    void createNewCourse_DifferentFacilityTypes_ThrowsException() {
        // Given
        CourseCreateRequest request = createValidCourseCreateRequest();
        Instructor instructor = createInstructor();
        List<Facility> facilities = createFacilities();
        facilities.add(createNotValidFacility(FacilityType.GYMNASIUM));

        when(instructorRepository.findById(request.getInstructorId())).thenReturn(java.util.Optional.of(instructor));
        when(facilityRepository.findAllById(request.getFacilitiesId())).thenReturn(facilities);
        when(courseRepository.findOverlappingCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.anyList()))
                .thenReturn(List.of());

        when(courseRepository.findOverlappingInstructorCourses(
                ArgumentMatchers.any(DayOfWeek.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(LocalTime.class),
                ArgumentMatchers.any(Instructor.class)))
                .thenReturn(List.of());

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.createNewCourse(request));
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
                .facilitiesId(List.of(
                        UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff91"),
                        UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff92"))) //Valid facilities ID
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
                .facilities(Collections.singletonList(createNotValidFacility(FacilityType.POOL)))
                .instructor(createInstructor())
                .build();
    }
    private Facility createNotValidFacility(FacilityType facilityType) {
        return Facility.builder()
                .id(UUID.randomUUID())
                .facilityType(facilityType)
                .name("Test Facility")
                .description("Description of Test Facility")
                .openingHoursStart(Collections.singletonMap(DayOfWeek.MONDAY, LocalTime.of(6, 30)))
                .openingHoursEnd(Collections.singletonMap(DayOfWeek.MONDAY, LocalTime.of(21, 30)))
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

    private List<Facility> createFacilities() {
        Map<DayOfWeek, LocalTime> openingHoursStart = new HashMap<>();
        Map<DayOfWeek, LocalTime> openingHoursEnd = new HashMap<>();

        LocalTime openingTime = LocalTime.of(6, 30);
        LocalTime closingTimeWeekdays = LocalTime.of(21, 30);
        LocalTime closingTimeSunday = LocalTime.of(20, 30);

        for (DayOfWeek day : DayOfWeek.values()) {
            openingHoursStart.put(day, openingTime);
            openingHoursEnd.put(day, (day == DayOfWeek.SUNDAY) ? closingTimeSunday : closingTimeWeekdays);
        }

        Facility facility1 = Facility.builder()
                .id(UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff91"))
                .facilityType(FacilityType.POOL)
                .name("Pool Line 1")
                .description("Line 2 description")
                .openingHoursStart(openingHoursStart)
                .openingHoursEnd(openingHoursEnd)
                .build();

        Facility facility2 = Facility.builder()
                .id(UUID.fromString("cee44ecd-d61c-4921-8a66-07dc38beff92"))
                .facilityType(FacilityType.POOL)
                .name("Pool Line 2")
                .description("Line 2 description")
                .openingHoursStart(openingHoursStart)
                .openingHoursEnd(openingHoursEnd)
                .build();

        return new ArrayList<>(Arrays.asList(facility1, facility2));
    }
}