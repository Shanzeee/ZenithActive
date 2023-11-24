package com.brvsk.ZenithActive.facility;

import java.util.List;
import java.util.UUID;

public interface FacilityService {
    List<FacilityResponse> getAllFacilities();
    FacilityResponse getFacilityById(UUID facilityId);
    Facility addFacility(FacilityRequest request);
    void deleteFacility(UUID facilityId);
}
