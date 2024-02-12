package com.brvsk.ZenithActive.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WeatherMapper {

    private final ObjectMapper objectMapper;

    public WeatherResponse mapToWeatherSummary(String jsonResponse) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        WeatherResponse summary = new WeatherResponse();
        summary.setResolvedAddress(rootNode.path("resolvedAddress").asText());
        summary.setDescription(rootNode.path("description").asText());
        summary.setDays(mapDays(rootNode.path("days")));
        summary.setAlerts(mapAlerts(rootNode.path("alerts")));

        return summary;
    }

    private List<DaySummary> mapDays(JsonNode daysNode) {
        List<DaySummary> days = new ArrayList<>();
        daysNode.forEach(dayNode -> days.add(mapDaySummary(dayNode)));
        return days;
    }

    private DaySummary mapDaySummary(JsonNode dayNode) {
        DaySummary day = new DaySummary();
        day.setDatetime(dayNode.path("datetime").asText());
        day.setTemp(dayNode.path("temp").asDouble());
        day.setFeelsLike(dayNode.path("feelslike").asDouble());
        day.setHumidity(dayNode.path("humidity").asDouble());
        day.setPrecipCover(dayNode.path("precipcover").asDouble());

        List<String> preciptype = new ArrayList<>();
        dayNode.path("preciptype").forEach(pt -> preciptype.add(pt.asText()));
        day.setPrecipType(preciptype);

        day.setWindSpeed(dayNode.path("windspeed").asDouble());
        day.setDescription(dayNode.path("description").asText());
        day.setIcon(dayNode.path("icon").asText());
        day.setHours(mapHours(dayNode.path("hours")));

        return day;
    }

    private List<HourSummary> mapHours(JsonNode hoursNode) {
        List<HourSummary> hours = new ArrayList<>();
        hoursNode.forEach(hourNode -> hours.add(mapHourSummary(hourNode)));
        return hours;
    }

    private HourSummary mapHourSummary(JsonNode hourNode) {
        HourSummary hour = new HourSummary();
        hour.setDatetime(hourNode.path("datetime").asText());
        hour.setTemp(hourNode.path("temp").asDouble());
        hour.setFeelsLike(hourNode.path("feelslike").asDouble());
        hour.setHumidity(hourNode.path("humidity").asDouble());
        hour.setWindSpeed(hourNode.path("windspeed").asDouble());
        hour.setIcon(hourNode.path("icon").asText());

        return hour;
    }

    private List<AlertSummary> mapAlerts(JsonNode alertsNode) {
        List<AlertSummary> alerts = new ArrayList<>();
        alertsNode.forEach(alertNode -> {
            AlertSummary alert = new AlertSummary();
            alert.setEvent(alertNode.path("event").asText());
            alert.setHeadline(alertNode.path("headline").asText());

            alerts.add(alert);
        });
        return alerts;
    }
}
