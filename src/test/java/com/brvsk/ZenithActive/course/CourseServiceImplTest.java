package com.brvsk.ZenithActive.course;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseMapper courseMapper;


    @Test
    void createNewCourse_Valid() {
        // Given
        CourseCreateRequest request = crateCourseCreateRequest();

        // When
        courseService.createNewCourse(request);

        // Then
        verify(courseRepository, times(1)).save(Mockito.any());
    }

    @Test
    void getAllCourses_Valid() {
        // Given
        UUID course1Id = UUID.randomUUID();
        Course course1 = createCourse(course1Id);

        UUID course2Id = UUID.randomUUID();
        Course course2 = createCourse(course2Id);

        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        // When
        List<CourseResponse> result = courseService.getAllCourses();

        // Then
        assertEquals(2, result.size());
        verify(courseMapper, times(2)).mapToResponse(Mockito.any());
    }

    @Test
    void getAllCourses_EmptyList() {
        // Given
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CourseResponse> result = courseService.getAllCourses();

        // Then
        assertTrue(result.isEmpty());
        verify(courseMapper, never()).mapToResponse(Mockito.any());
    }

    @Test
    void getCoursesForCourseType_Valid() {
        // Given
        CourseType courseType = CourseType.ABT;

        UUID course1Id = UUID.randomUUID();
        Course course1 = createCourse(course1Id);

        UUID course2Id = UUID.randomUUID();
        Course course2 = createCourse(course2Id);

        UUID course3Id = UUID.randomUUID();
        Course course3 = createCourse(course3Id);

        List<Course> courses = Arrays.asList(course1, course2, course3);

        // When
        when(courseRepository.getCoursesByCourseType(courseType)).thenReturn(courses);
        List<CourseResponse> result = courseService.getCoursesForCourseType(courseType);

        // Then
        assertEquals(3, result.size());
        verify(courseMapper, times(3)).mapToResponse(Mockito.any());
    }

    @Test
    void getCoursesForCourseType_NoMatchingCourses() {
        // Given
        CourseType courseType = CourseType.ABT;

        // When
        when(courseRepository.getCoursesByCourseType(courseType)).thenReturn(Collections.emptyList());
        List<CourseResponse> result = courseService.getCoursesForCourseType(courseType);

        // Then
        assertTrue(result.isEmpty());
        verify(courseMapper, never()).mapToResponse(Mockito.any());
    }



    private CourseCreateRequest crateCourseCreateRequest() {
        return CourseCreateRequest.builder()
                .name("course")
                .description("description of course")
                .courseType(CourseType.ZUMBA)
                .build();
    }

    private Course createCourse(UUID uuid) {
        return Course
                .builder()
                .id(uuid)
                .courseType(CourseType.ABT)
                .description("course description")
                .name("course name")
                .build();
    }
}