package com.example.Weather.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherSummaryResponse {
    private double avgPressure;
    private double avgSunshine;
    private double minTemperature;
    private double maxTemperature;
    private String summary;
}

