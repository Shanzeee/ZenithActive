package com.brvsk.ZenithActive.facility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class FacilityResponse {
    private UUID id;
    private FacilityType facilityType;
    private String name;
    private String description;
    private Map<DayOfWeek,String> openHours;
}
