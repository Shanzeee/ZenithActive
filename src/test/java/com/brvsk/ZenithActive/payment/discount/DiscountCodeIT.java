package com.brvsk.ZenithActive.payment.discount;

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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DiscountCodeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @BeforeEach
    void setUp() {
        DiscountCode discountCode = DiscountCode.builder()
                .code("TESTCODE")
                .discountPercentage(20)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();
        discountCodeRepository.save(discountCode);
    }

    @AfterEach
    void tearDown() {
        discountCodeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDiscountCode_Success() throws Exception {
        DiscountCodeCreateRequest request = new DiscountCodeCreateRequest("NEWCODE", 15, LocalDate.now(), LocalDate.now().plusDays(30));

        mockMvc.perform(post("/api/v1/discount-codes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Discount code created successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDiscountCode_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/discount-codes/{code}", "TESTCODE"))
                .andExpect(status().isOk())
                .andExpect(content().string("Discount code deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void calculateDiscountPercentage_Success() throws Exception {
        mockMvc.perform(get("/api/v1/discount-codes/calculate/{code}", "TESTCODE"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDiscountCodes_Success() throws Exception {
        mockMvc.perform(get("/api/v1/discount-codes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("TESTCODE"))
                .andExpect(jsonPath("$[0].discountPercentage").value(20));
    }
}
