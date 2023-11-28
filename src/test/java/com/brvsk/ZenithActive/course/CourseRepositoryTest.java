package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.Speciality;
import com.brvsk.ZenithActive.user.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findOverlappingCourses() {
        // Given
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        List<Facility> facilities = prepareFacilities();

        // When
        List<Course> overlappingCourses = courseRepository.findOverlappingCourses(dayOfWeek, startTime, endTime, facilities);

        // Then
        assertNotNull(overlappingCourses);
    }

    @Test
    void findOverlappingInstructorCourses() {
        // Given
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        Instructor instructor = prepareInstructor();

        // When
        List<Course> overlappingInstructorCourses = courseRepository.findOverlappingInstructorCourses(dayOfWeek, startTime, endTime, instructor);

        // Then
        assertNotNull(overlappingInstructorCourses);
    }

    private List<Facility> prepareFacilities() {
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

    private Instructor prepareInstructor() {
        Instructor instructor = new Instructor();
        instructor.setUserId(UUID.fromString("eb5876df-bc26-4232-8779-172c1b341700"));
        instructor.setFirstName("Kacper");
        instructor.setLastName("Doe");
        instructor.setGender(Gender.MALE);
        instructor.setDescription("Some description");
        instructor.setSpecialities(Collections.singletonList(Speciality.YOGA));
        return instructor;
    }
}