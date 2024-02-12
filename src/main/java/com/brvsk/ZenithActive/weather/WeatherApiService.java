package com.brvsk.ZenithActive.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final RestTemplate restTemplate;
    private final WeatherApiConfig weatherApiConfig;
    private final WeatherMapper weatherMapper;


    public WeatherResponse getWeatherForToday() {
        return fetchWeatherData("today");
    }

    public WeatherResponse getWeatherForTomorrow() {
        return fetchWeatherData("tomorrow");
    }

    public WeatherResponse getWeatherForDate(LocalDate date) {
        return fetchWeatherData(date.toString());
    }


    private WeatherResponse fetchWeatherData(String urlDetails) {
        String apiUrl = weatherApiConfig.getApiUrl();
        String apiKey = weatherApiConfig.getApiKey();
        String url = apiUrl + urlDetails + "?key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        try {
            return weatherMapper.mapToWeatherSummary(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data from " + url, e);
        }
    }

}
