package com.brvsk.ZenithActive.membership;

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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MembershipIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MemberRepository memberRepository;

    private UUID memberId;

    @BeforeEach
    void setUp() {
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        member = memberRepository.save(member);
        memberId = member.getUserId();
    }

    @AfterEach
    void tearDown() {
        membershipRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addNewMembershipToMember_Success() throws Exception {
        MembershipRequest request = new MembershipRequest(
                MembershipType.FULL,
                1,
                memberId
        );

        mockMvc.perform(post("/api/v1/membership/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Membership created"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addNewMembershipToMember_UserNotFound() throws Exception {
        MembershipRequest request = new MembershipRequest(
                MembershipType.FULL,
                1,
                UUID.randomUUID()
        );

        mockMvc.perform(post("/api/v1/membership/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}