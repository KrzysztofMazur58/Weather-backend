package com.example.Weather.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WeatherResponse {

    private List<DailyForecast> forecasts;

}
