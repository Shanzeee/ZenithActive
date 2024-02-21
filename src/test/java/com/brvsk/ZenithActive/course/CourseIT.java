package com.brvsk.ZenithActive.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    private static final String PATH = "/api/v1/courses";

    @BeforeEach
    void setUp() {
        Course course1 = new Course(UUID.randomUUID(), CourseType.ZUMBA, "Zumba Beginners", "Zumba for beginners", new HashSet<>());
        Course course2 = new Course(UUID.randomUUID(), CourseType.PILATES, "Pilates Intermediate", "Intermediate Pilates course", new HashSet<>());
        Course course3 = new Course(UUID.randomUUID(), CourseType.PILATES, "Pilates Advanced", "Advanced Pilates course", new HashSet<>());
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void canCreateNewCourse() throws Exception {
        CourseCreateRequest request = new CourseCreateRequest(
                CourseType.ZUMBA,
                "Zumba course",
                "description for zumba course"
        );

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Course created successfully"));
    }

    @Test
    void canGetAllCourses() throws Exception {
        mockMvc.perform(get(PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void canGetCoursesForCourseType() throws Exception {
        mockMvc.perform(get(PATH + "/byCourseType/ZUMBA")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].courseType").value("ZUMBA"))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}

