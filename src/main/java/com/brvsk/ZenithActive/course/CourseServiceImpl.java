package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityNotFoundException;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final FacilityRepository facilityRepository;
    private final CourseMapper courseMapper;

    @Override
    public void createNewCourse(CourseCreateRequest request){
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new UserNotFoundException(request.getInstructorId()));

        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new FacilityNotFoundException(request.getFacilityId()));


        validateCourseHours(request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), facility);
        validateInstructorAvailability(request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), instructor);

        Course courseToAdd = toEntity(request);
        courseToAdd.setFacility(facility);
        courseToAdd.setInstructor(instructor);
        courseRepository.save(courseToAdd);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesForCourseType(CourseType courseType) {
        return courseRepository.getCoursesByCourseType(courseType)
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesForInstructor(UUID instructorId) {
        return courseRepository.getCoursesByInstructor_UserId(instructorId)
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateCourseHours(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Facility facility) {
        List<Course> overlappingCourses = courseRepository.findOverlappingCourses(dayOfWeek, startTime, endTime, facility);

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
