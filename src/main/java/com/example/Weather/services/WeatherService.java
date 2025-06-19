package com.example.Weather.services;

import com.example.Weather.responses.DailyForecast;
import com.example.Weather.responses.WeatherResponse;
import com.example.Weather.responses.WeatherSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeatherForecast(double latitude, double longitude) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("daily", "temperature_2m_min,temperature_2m_max,weather_code,sunshine_duration")
                .queryParam("timezone", "auto")
                .toUriString();

        Map response = restTemplate.getForObject(url, Map.class);
        Map<String, List> daily = (Map<String, List>) response.get("daily");

        List<String> dates = daily.get("time");
        List<Double> minTemps = daily.get("temperature_2m_min");
        List<Double> maxTemps = daily.get("temperature_2m_max");
        List<Integer> weatherCodes = daily.get("weather_code");
        List<Double> sunshineDurations = daily.get("sunshine_duration");

        double mocInstalacji = 2.5;
        double efektywnosc = 0.2;

        List<DailyForecast> forecasts = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            double sunshineHours = sunshineDurations.get(i) / 60.0;
            double energy = mocInstalacji * sunshineHours * efektywnosc;

            DailyForecast forecast = new DailyForecast(
                    dates.get(i),
                    weatherCodes.get(i),
                    minTemps.get(i),
                    maxTemps.get(i),
                    Math.round(energy * 100.0) / 100.0
            );
            forecasts.add(forecast);
        }

        return new WeatherResponse(forecasts);
    }

    public WeatherSummaryResponse getWeeklySummary(double latitude, double longitude) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("daily", "temperature_2m_min,temperature_2m_max,sunshine_duration,weather_code,surface_pressure_mean")
                .queryParam("timezone", "auto")
                .toUriString();

        Map response = restTemplate.getForObject(url, Map.class);
        Map<String, List> daily = (Map<String, List>) response.get("daily");

        List<Double> minTemps = daily.get("temperature_2m_min");
        List<Double> maxTemps = daily.get("temperature_2m_max");
        List<Double> sunshine = daily.get("sunshine_duration");
        List<Double> pressure = daily.get("surface_pressure_mean");
        List<Integer> weatherCodes = daily.get("weather_code");

        double avgPressure = pressure.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double avgSunshine = sunshine.stream().mapToDouble(Double::doubleValue).average().orElse(0) / 3600.0;

        double minTemp = minTemps.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double maxTemp = maxTemps.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        long rainyDays = weatherCodes.stream()
                .filter(code -> code >= 51 && code <= 99)
                .count();

        String summary = (rainyDays >= 4) ? "z opadami" : "bez opadów";

        return new WeatherSummaryResponse(
                Math.round(avgPressure * 100.0) / 100.0,
                Math.round(avgSunshine * 100.0) / 100.0,
                minTemp,
                maxTemp,
                summary
        );
    }

}

