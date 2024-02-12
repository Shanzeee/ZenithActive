package com.brvsk.ZenithActive.weather;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DaySummary {
    private String datetime;
    private double temp;
    private double feelsLike;
    private double humidity;
    private double precipCover;
    private List<String> precipType;
    private double windSpeed;
    private String description;
    private String icon;
    private List<HourSummary> hours;

}
