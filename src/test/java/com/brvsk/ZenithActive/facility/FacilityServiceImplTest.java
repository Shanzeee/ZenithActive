package com.brvsk.ZenithActive.facility;

import com.brvsk.ZenithActive.excpetion.FacilityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class FacilityServiceImplTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private FacilityMapper facilityMapper;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void itShouldGetAllFacilities() {
        // Given
        Facility facility = createSampleFacility();
        FacilityResponse facilityResponse = createSampleFacilityResponse();

        when(facilityRepository.findAll()).thenReturn(Collections.singletonList(facility));
        when(facilityMapper.mapToResponse(any())).thenReturn(facilityResponse);

        // When
        List<FacilityResponse> result = facilityService.getAllFacilities();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        FacilityResponse resultFacilityResponse = result.get(0);
        assertThat(resultFacilityResponse).isEqualTo(facilityResponse);

        verify(facilityRepository, times(1)).findAll();
        verify(facilityMapper, times(1)).mapToResponse(any());
    }

    @Test
    void itShouldGetFacilityById() {
        // Given
        UUID facilityId = UUID.randomUUID();
        Facility facility = createSampleFacility();
        FacilityResponse facilityResponse = createSampleFacilityResponse();

        when(facilityRepository.findById(facilityId)).thenReturn(java.util.Optional.of(facility));
        when(facilityMapper.mapToResponse(any())).thenReturn(facilityResponse);

        // When
        FacilityResponse result = facilityService.getFacilityById(facilityId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(facilityResponse);

        verify(facilityRepository, times(1)).findById(facilityId);
        verify(facilityMapper, times(1)).mapToResponse(any());
    }

    @Test
    void itShouldAddFacility() {
        // Given
        FacilityRequest request = createSampleFacilityRequest();
        Facility facility = createSampleFacility();

        when(facilityRepository.save(any())).thenReturn(facility);

        // When
        Facility result = facilityService.addFacility(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(facility);

        verify(facilityRepository, times(1)).save(any());
    }

    @Test
    void itShouldDeleteFacility() {
        // Given
        UUID facilityId = UUID.randomUUID();
        Facility facility = createSampleFacility();

        when(facilityRepository.findById(facilityId)).thenReturn(java.util.Optional.of(facility));

        // Act
        facilityService.deleteFacility(facilityId);

        // Verify
        verify(facilityRepository, times(1)).findById(facilityId);
        verify(facilityRepository, times(1)).delete(facility);
    }

    @Test
    void itShouldThrowFacilityNotFoundException() {
        // Given
        UUID nonExistingFacilityId = UUID.randomUUID();

        when(facilityRepository.findById(nonExistingFacilityId)).thenReturn(java.util.Optional.empty());

        // When
        assertThrows(FacilityNotFoundException.class, () -> facilityService.deleteFacility(nonExistingFacilityId));

        // Then
        verify(facilityRepository, times(1)).findById(nonExistingFacilityId);
        verify(facilityRepository, never()).delete(any());
    }

    private Facility createSampleFacility() {
        return Facility.builder()
                .id(UUID.randomUUID())
                .facilityType(FacilityType.POOL)
                .name("Sample Facility")
                .description("Description")
                .openingHoursStart(Collections.emptyMap())
                .openingHoursEnd(Collections.emptyMap())
                .build();
    }

    private FacilityResponse createSampleFacilityResponse() {
        return FacilityResponse.builder()
                .id(UUID.randomUUID())
                .facilityType(FacilityType.POOL)
                .name("Sample Facility")
                .description("Description")
                .openHours(Collections.emptyMap())
                .build();
    }

    private FacilityRequest createSampleFacilityRequest() {
        return FacilityRequest.builder()
                .facilityType(FacilityType.POOL)
                .name("Sample Facility")
                .description("Description")
                .openingHoursStart(Collections.emptyMap())
                .openingHoursEnd(Collections.emptyMap())
                .build();
    }
}