package com.brvsk.ZenithActive.facility;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FacilityMapper {

    public FacilityResponse mapToResponse(Facility facility) {
        return FacilityResponse.builder()
                .id(facility.getId())
                .facilityType(facility.getFacilityType())
                .name(facility.getName())
                .description(facility.getDescription())
                .openHours(mapOpenHours(facility.getOpeningHoursStart(), facility.getOpeningHoursEnd()))
                .build();
    }

    private Map<DayOfWeek, String> mapOpenHours(Map<DayOfWeek, LocalTime> openingHoursStart, Map<DayOfWeek, LocalTime> openingHoursEnd) {
        if (openingHoursStart == null || openingHoursEnd == null) {
            return Collections.emptyMap();
        }

        return openingHoursStart.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> formatOpeningHours(entry.getValue(), openingHoursEnd.get(entry.getKey()))
                ));
    }

    private String formatOpeningHours(LocalTime openingHoursStart, LocalTime openingHoursEnd) {
        if (openingHoursStart == null || openingHoursEnd == null) {
            return "CLOSED";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        String formattedStart = openingHoursStart.format(formatter);
        String formattedEnd = openingHoursEnd.format(formatter);

        return formattedStart + " - " + formattedEnd;
    }
}
