package com.brvsk.ZenithActive.facility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class FacilityRequest {
    @NotNull
    private FacilityType facilityType;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Map<DayOfWeek, LocalTime> openingHoursStart;
    private Map<DayOfWeek, LocalTime> openingHoursEnd;

}
