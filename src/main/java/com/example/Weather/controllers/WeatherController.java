package com.example.Weather.controllers;

import com.example.Weather.responses.WeatherResponse;
import com.example.Weather.responses.WeatherSummaryResponse;
import com.example.Weather.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/forecast")
@CrossOrigin(origins = "https://weather-frontend-vic2.onrender.com")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/daily")
    public ResponseEntity<WeatherResponse> get7DayForecast(
            @RequestParam @NotNull @Min(-90) @Max(90) Double latitude,
            @RequestParam @NotNull @Min(-180) @Max(180) Double longitude
    ) {
        WeatherResponse forecast = weatherService.getWeatherForecast(latitude, longitude);
        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/summary")
    public ResponseEntity<WeatherSummaryResponse> getWeeklySummary(
            @RequestParam @NotNull @Min(-90) @Max(90) Double latitude,
            @RequestParam @NotNull @Min(-180) @Max(180) Double longitude
    ) {
        WeatherSummaryResponse summary = weatherService.getWeeklySummary(latitude, longitude);
        return ResponseEntity.ok(summary);
    }

}
