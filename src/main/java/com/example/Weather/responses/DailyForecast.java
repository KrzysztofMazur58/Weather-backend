package com.example.Weather.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast {
    public String date;
    public int weatherCode;
    public double minTemp;
    public double maxTemp;
    public double energyKWh;
}

