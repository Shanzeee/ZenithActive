package com.brvsk.ZenithActive.facility;

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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacilityIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FacilityRepository facilityRepository;

    private static final String PATH = "/api/v1/facilities";

    @BeforeEach
    void setUp() {
        Map<DayOfWeek, LocalTime> openingHoursStart = new HashMap<>();
        Map<DayOfWeek, LocalTime> openingHoursEnd = new HashMap<>();
        openingHoursStart.put(DayOfWeek.MONDAY, LocalTime.of(9, 0));
        openingHoursEnd.put(DayOfWeek.MONDAY, LocalTime.of(17, 0));

        Facility facility = Facility.builder()
                .facilityType(FacilityType.GYM)
                .name("Local Gym")
                .description("A local gym")
                .openingHoursStart(openingHoursStart)
                .openingHoursEnd(openingHoursEnd)
                .build();

        facilityRepository.save(facility);

        Facility facility2 = Facility.builder()
                .facilityType(FacilityType.GYM)
                .name("Local Gym2")
                .description("A local gym2")
                .openingHoursStart(openingHoursStart)
                .openingHoursEnd(openingHoursEnd)
                .build();

        facilityRepository.save(facility2);
    }

    @AfterEach
    void tearDown() {
        facilityRepository.deleteAll();
    }

    @Test
    void getAllFacilities() throws Exception {
        mockMvc.perform(get(PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Local Gym"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getFacilityById() throws Exception {
        List<Facility> facilities = facilityRepository.findAll();
        Facility facility = facilities.get(0);

        mockMvc.perform(get(PATH + "/" + facility.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Local Gym"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addFacility() throws Exception {
        FacilityRequest facilityRequest = new FacilityRequest(FacilityType.GYM, "New Gym", "A new gym", new HashMap<>(), new HashMap<>());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facilityRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Gym"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFacility() throws Exception {
        Facility facility = facilityRepository.findAll().iterator().next();
        mockMvc.perform(delete(PATH + "/" + facility.getId()))
                .andExpect(status().isNoContent());
    }
}
