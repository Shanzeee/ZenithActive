package com.brvsk.ZenithActive.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public void createNewCourse(CourseCreateRequest request) {
        Course course = toEntity(request);
        courseRepository.save(course);
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

    private Course toEntity(CourseCreateRequest request){
        return Course.builder()
                .courseType(request.getCourseType())
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
