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

    @NotNull(message = "Facility type cannot be null")
    private FacilityType facilityType;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private Map<DayOfWeek, LocalTime> openingHoursStart;

    private Map<DayOfWeek, LocalTime> openingHoursEnd;
}
