package com.example.Weather;

import com.example.Weather.controllers.WeatherController;
import com.example.Weather.responses.DailyForecast;
import com.example.Weather.responses.WeatherResponse;
import com.example.Weather.responses.WeatherSummaryResponse;
import com.example.Weather.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@Import(WeatherControllerTest.WeatherServiceTestConfig.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @TestConfiguration
    static class WeatherServiceTestConfig {
        @Bean
        public WeatherService weatherService() {
            return Mockito.mock(WeatherService.class);
        }
    }

    @Test
    void shouldReturnForecast() throws Exception {
        Mockito.when(weatherService.getWeatherForecast(anyDouble(), anyDouble()))
                .thenReturn(new WeatherResponse(List.of(
                        new DailyForecast("2025-06-19", 3, 15.3, 21.1, 4.2)
                )));

        mockMvc.perform(get("/forecast/daily")
                        .param("latitude", "52.0")
                        .param("longitude", "21.0"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSummary() throws Exception {
        Mockito.when(weatherService.getWeeklySummary(anyDouble(), anyDouble()))
                .thenReturn(new WeatherSummaryResponse(1010.0, 4.5, 10.0, 25.0, "bez opadów"));

        mockMvc.perform(get("/forecast/summary")
                        .param("latitude", "52.0")
                        .param("longitude", "21.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgPressure").value(1010.0))
                .andExpect(jsonPath("$.avgSunshine").value(4.5));
    }
}


