package com.brvsk.ZenithActive.facility;

import com.brvsk.ZenithActive.excpetion.FacilityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    @Override
    public List<FacilityResponse> getAllFacilities() {
        return facilityRepository
                .findAll()
                .stream()
                .map(facilityMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FacilityResponse getFacilityById(UUID facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityNotFoundException(facilityId));

        return facilityMapper.mapToResponse(facility);

    }

    @Override
    public Facility addFacility(FacilityRequest request) {
        Facility facility = toEntity(request);
        return facilityRepository.save(facility);
    }

    @Override
    public void deleteFacility(UUID facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityNotFoundException(facilityId));
        facilityRepository.delete(facility);
    }

    private Facility toEntity(FacilityRequest request) {
        return Facility.builder()
                .id(UUID.randomUUID())
                .facilityType(request.getFacilityType())
                .name(request.getName())
                .description(request.getDescription())
                .openingHoursStart(request.getOpeningHoursStart())
                .openingHoursEnd(request.getOpeningHoursEnd())
                .build();
    }
}
