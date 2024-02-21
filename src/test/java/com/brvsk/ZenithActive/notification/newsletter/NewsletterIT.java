package com.brvsk.ZenithActive.notification.newsletter;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NewsletterIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsletterRepository newsletterRepository;

    private final String testEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        NewsletterSubscriber subscriber = new NewsletterSubscriber(UUID.randomUUID(), "TestName", testEmail, false);
        newsletterRepository.save(subscriber);
    }

    @AfterEach
    void tearDown() {
        newsletterRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void confirmNewsletterSubscription_Success() throws Exception {
        mockMvc.perform(get("/api/v1/newsletter/confirm/{email}", testEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("thanks for subscribing"));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void confirmNewsletterSubscription_SubscriberNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/newsletter/confirm/{email}", "nonexistent@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Subscriber not found"));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void unsubscribeNewsletter_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/newsletter/unsubscribe/{email}", testEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Unsubscribed successfully"));
    }
}
