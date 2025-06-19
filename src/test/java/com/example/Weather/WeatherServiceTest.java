package com.example.Weather;

import com.example.Weather.responses.WeatherResponse;
import com.example.Weather.responses.WeatherSummaryResponse;
import com.example.Weather.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherService weatherService;
    private RestTemplate mockRestTemplate;

    @BeforeEach
    void setUp() {
        mockRestTemplate = Mockito.mock(RestTemplate.class);
        weatherService = new WeatherService(mockRestTemplate);
    }

    @Test
    void testWeeklySummaryComputation() {
        Map<String, List> daily = Map.of(
                "temperature_2m_min", List.of(10.0, 12.0, 11.5, 13.0, 9.0),
                "temperature_2m_max", List.of(20.0, 22.0, 21.0, 23.0, 19.0),
                "sunshine_duration", List.of(3600.0, 7200.0, 1800.0, 3600.0, 0.0),
                "surface_pressure_mean", List.of(1000.0, 1005.0, 995.0, 1003.0, 1002.0),
                "weather_code", List.of(53, 3, 51, 63, 80)
        );
        Map<String, Object> response = Map.of("daily", daily);

        Mockito.when(mockRestTemplate.getForObject(Mockito.anyString(), Mockito.eq(Map.class)))
                .thenReturn(response);

        WeatherSummaryResponse summary = weatherService.getWeeklySummary(52.0, 21.0);

        assertEquals("z opadami", summary.getSummary());
        assertEquals(9.0, summary.getMinTemperature());
        assertEquals(23.0, summary.getMaxTemperature());
        assertEquals(1001.0, summary.getAvgPressure());
        assertTrue(summary.getAvgSunshine() > 0);
    }

}

