package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public void createNewCourse(CourseCreateRequest request){
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new UserNotFoundException(request.getInstructorId()));

        List<Facility> facilities = facilityRepository.findAllById(request.getFacilitiesId());
        validateFacilityTypes(facilities);

        validateCourseHours(request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), facilities);
        validateInstructorAvailability(request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), instructor);


        Course courseToAdd = toEntity(request);
        courseToAdd.setFacilities(facilities);
        courseToAdd.setInstructor(instructor);
        courseRepository.save(courseToAdd);
    }

    private void validateCourseHours(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, List<Facility> facilities) {
        List<Course> overlappingCourses = courseRepository.findOverlappingCourses(dayOfWeek, startTime, endTime, facilities);

        if (!overlappingCourses.isEmpty()) {
            throw new IllegalArgumentException("The course hours overlap with existing courses.");
        }
    }

    private void validateInstructorAvailability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Instructor instructor) {
        List<Course> overlappingCourses = courseRepository.findOverlappingInstructorCourses(dayOfWeek, startTime, endTime, instructor);

        if (!overlappingCourses.isEmpty()) {
            throw new IllegalArgumentException("The instructor is not available during the specified hours.");
        }
    }

    private void validateFacilityTypes(List<Facility> facilities) {
        if (facilities.size() > 1) {
            FacilityType firstFacilityType = facilities.get(0).getFacilityType();
            for (Facility facility : facilities) {
                if (facility.getFacilityType() != firstFacilityType) {
                    throw new IllegalArgumentException("All facilities must have the same type.");
                }
            }
        }
    }

    private Course toEntity(CourseCreateRequest request){
        return Course.builder()
                .courseType(request.getCourseType())
                .name(request.getName())
                .description(request.getDescription())
                .groupSize(request.getGroupSize())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
