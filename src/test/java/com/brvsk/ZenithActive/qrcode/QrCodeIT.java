package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QrCodeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;
    private UUID validUserId;
    private UUID invalidUserId;

    @BeforeEach
    void setUp() {
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        memberRepository.save(member);
        validUserId = member.getUserId();
        invalidUserId = UUID.randomUUID();
    }


    @Test
    @WithMockUser(roles = "MEMBER")
    void generateQrCode_Success() throws Exception {
        mockMvc.perform(get("/api/v1/qrcode/generate/{userId}", validUserId)
                        .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void generateQrCode_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/qrcode/generate/{userId}", invalidUserId))
                .andExpect(status().isNotFound());
    }
}