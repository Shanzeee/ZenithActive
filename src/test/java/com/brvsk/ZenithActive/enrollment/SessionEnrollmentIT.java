package com.brvsk.ZenithActive.enrollment;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.course.CourseType;
import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.facility.FacilityType;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.membership.MembershipType;
import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.session.SessionRepository;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SessionEnrollmentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private UUID validSessionId;
    private UUID validMemberId;

    @BeforeEach
    void setUp() {
        Course course = new Course(UUID.randomUUID(), CourseType.ABT,"ABT","desc",new HashSet<>());
        course = courseRepository.saveAndFlush(course);

        Facility facility = new Facility(UUID.randomUUID(), FacilityType.POOL,"pool","desc",new HashMap<>(),new HashMap<>());
        facility = facilityRepository.saveAndFlush(facility);

        Instructor instructor = new Instructor();
        instructor.setUserId(UUID.randomUUID());
        instructor = instructorRepository.saveAndFlush(instructor);

        Session session = new Session(
                UUID.randomUUID(),
                course,
                facility,
                instructor,
                10,
                LocalDate.now().plusDays(1),
                LocalTime.NOON,
                LocalTime.NOON.plusHours(1),
                new HashSet<>());
        session = sessionRepository.saveAndFlush(session);
        validSessionId = session.getId();

        Member member = new Member();
        Membership membership = new Membership(
                UUID.randomUUID(),
                MembershipType.FULL,
                LocalDate.now(),
                LocalDate.now().plusDays(20),
                member);
        member.setMemberships(List.of(membership));
        member.setUserId(UUID.randomUUID());
        member.setEmail("email@test.com");
        member = memberRepository.saveAndFlush(member);
        validMemberId = member.getUserId();
    }

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void enrollMemberToSession_Success() throws Exception {
        mockMvc.perform(post("/api/v1/session_enrollments/enroll")
                        .param("sessionId", validSessionId.toString())
                        .param("memberId", validMemberId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Member enrolled to session successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSessionsForMember_Success() throws Exception {
        mockMvc.perform(get("/api/v1/session_enrollments/byMemberId/{userId}", validMemberId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getMembersForSession_Success() throws Exception {
        mockMvc.perform(get("/api/v1/session_enrollments/{sessionId}/members", validSessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
