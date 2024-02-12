package com.brvsk.ZenithActive.weather;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {
    private String resolvedAddress;
    private String description;
    private List<DaySummary> days;
    private List<AlertSummary> alerts;
}
