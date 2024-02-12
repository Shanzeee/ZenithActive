package com.brvsk.ZenithActive.weather;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourSummary {
    private String datetime;
    private double temp;
    private double feelsLike;
    private double humidity;
    private double windSpeed;
    private String icon;
}
