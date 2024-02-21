package com.brvsk.ZenithActive.loyalty;

import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoyaltyPointsIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoyaltyPointsRepository loyaltyPointsRepository;

    @Autowired
    private MemberRepository memberRepository;

    private UUID testMemberId;

    @BeforeEach
    void setUp() {
        Member testMember = new Member();
        testMember.setUserId(UUID.randomUUID());
        testMember = memberRepository.save(testMember);
        testMemberId = testMember.getUserId();
        LoyaltyPoints points = LoyaltyPoints.builder()
                .member(testMember)
                .pointsAmount(100)
                .loyaltyPointsType(LoyaltyPointsType.REVIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        loyaltyPointsRepository.save(points);
    }

    @AfterEach
    void tearDown() {
        loyaltyPointsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addLoyaltyPoints() throws Exception {
        LoyaltyPointsCreateRequest request = new LoyaltyPointsCreateRequest(
                LoyaltyPointsType.REVIEW,
                10,
                testMemberId
        );
        mockMvc.perform(post("/api/v1/loyalty-points/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Loyalty points added successfully"));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void getTotalPointsForMember() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty-points/total-points-new/{memberId}", testMemberId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void countGivenPointsToday() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty-points/count-given-points-today")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void countGivenPointsToday2() throws Exception {
        Member testMember = new Member();
        testMember.setUserId(UUID.randomUUID());
        testMember = memberRepository.save(testMember);
        testMemberId = testMember.getUserId();
        LoyaltyPoints points = LoyaltyPoints.builder()
                .member(testMember)
                .pointsAmount(100)
                .loyaltyPointsType(LoyaltyPointsType.REVIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        loyaltyPointsRepository.save(points);

        mockMvc.perform(get("/api/v1/loyalty-points/count-given-points-today")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
