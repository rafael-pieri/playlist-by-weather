package com.playlist.service.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.playlist.exception.weather.WeatherBadRequestException;
import com.playlist.exception.weather.WeatherNotFoundException;
import com.playlist.exception.weather.WeatherServiceUnavailableException;
import com.playlist.model.music.deezer.playlist.DeezerPlaylist;
import com.playlist.model.weather.Weather;
import com.playlist.model.weather.WeatherQuery;
import com.playlist.model.weather.open.Main;
import com.playlist.model.weather.open.OpenWeather;
import com.playlist.service.impl.weather.OpenWeatherMapService;

@RunWith(MockitoJUnitRunner.class)
public class OpenWeatherMapServiceTest {

    private static final String URL_ATTRIBUTE = "url";
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String AMERICANA = "Americana";
    private static final String BRAZIL = "br";

    @InjectMocks
    private OpenWeatherMapService openWeatherMapService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(this.openWeatherMapService, URL_ATTRIBUTE, OPEN_WEATHER_MAP_URL);
    }

    @Test
    public void shouldGetWeatherByCityAndCountry() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        final OpenWeather openWeather = new OpenWeather(new Main(27.0));

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenReturn(new ResponseEntity<>(openWeather, HttpStatus.OK));

        final Optional<Weather> optionalWeather = this.openWeatherMapService.getWeather(weatherQuery);

        assertTrue(optionalWeather.isPresent());
        assertEquals(Double.valueOf(27.0), optionalWeather.get().getTemperature());

        verify(this.restTemplate, times(1)).getForEntity(anyString(), Mockito.<Class<DeezerPlaylist>>any());
    }

    @Test
    public void shouldGetWeatherByLatitudeAndLongitude() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        final OpenWeather openWeather = new OpenWeather(new Main(31.0));

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenReturn(new ResponseEntity<>(openWeather, HttpStatus.OK));

        final Optional<Weather> optionalWeather = this.openWeatherMapService.getWeather(weatherQuery);

        assertTrue(optionalWeather.isPresent());
        assertEquals(Double.valueOf(31.0), optionalWeather.get().getTemperature());

        verify(this.restTemplate, times(1)).getForEntity(anyString(), Mockito.<Class<DeezerPlaylist>>any());
    }

    @Test(expected = WeatherNotFoundException.class)
    public void shouldThrowWeatherNotFoundException() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.openWeatherMapService.getWeather(weatherQuery);
    }

    @Test(expected = WeatherBadRequestException.class)
    public void shouldThrowWeatherBadRequestException() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        this.openWeatherMapService.getWeather(weatherQuery);
    }

    @Test(expected = WeatherServiceUnavailableException.class)
    public void shouldThrowWeatherServiceUnavailableException() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.openWeatherMapService.getWeather(weatherQuery);
    }

    @Test(expected = Exception.class)
    public void shouldThrowAnUnknownException() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        when(this.restTemplate.getForEntity(anyString(), Mockito.<Class<OpenWeather>>any()))
                .thenThrow(new IllegalArgumentException());

        this.openWeatherMapService.getWeather(weatherQuery);
    }

    private WeatherQuery getWeatherQueryByCityAndCountry() {
        return new WeatherQuery.Builder()
                .withNameCity(AMERICANA)
                .withCountry(BRAZIL)
                .build();
    }

    private WeatherQuery getWeatherQueryByLatitudeAndLongitude() {
        return new WeatherQuery.Builder()
                .withLatitude(BigDecimal.valueOf(-12.7))
                .withLongitude(BigDecimal.valueOf(8.9))
                .build();
    }
}