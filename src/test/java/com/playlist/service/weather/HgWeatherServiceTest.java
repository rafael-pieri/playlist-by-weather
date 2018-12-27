package com.playlist.service.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.playlist.model.weather.Weather;
import com.playlist.model.weather.WeatherQuery;
import com.playlist.model.weather.hg.HgWeather;
import com.playlist.model.weather.hg.Results;
import com.playlist.service.impl.weather.HgWeatherService;

@RunWith(MockitoJUnitRunner.class)
public class HgWeatherServiceTest {

    private static final String URL_ATTRIBUTE = "url";
    private static final String HG_BRAZIL_URL = "http://api.hgbrasil.com/weather/";
    private static final String AMERICANA = "Americana";
    private static final String BRAZIL = "br";

    @InjectMocks
    private HgWeatherService hgWeatherService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(this.hgWeatherService, URL_ATTRIBUTE, HG_BRAZIL_URL);
    }

    @Test
    public void shouldGetWeatherByCityAndCountry() {
        final WeatherQuery weatherQuery = getWeatherQueryByCityAndCountry();

        final HgWeather hgWeather = new HgWeather(new Results(27));

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenReturn(new ResponseEntity<>(hgWeather, HttpStatus.OK));

        final Optional<Weather> optionalWeather = this.hgWeatherService.getWeather(weatherQuery);

        assertTrue(optionalWeather.isPresent());
        assertEquals(Double.valueOf(27), optionalWeather.get().getTemperature());

        verify(this.restTemplate, times(1)).exchange(anyString(), Mockito.any(), Mockito.any(),
                Mockito.<Class<HgWeather>>any());
    }

    @Test
    public void shouldGetWeatherByLatitudeAndLongitude() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        final HgWeather hgWeather = new HgWeather(new Results(27));

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenReturn(new ResponseEntity<>(hgWeather, HttpStatus.OK));

        final Optional<Weather> optionalWeather = this.hgWeatherService.getWeather(weatherQuery);

        assertTrue(optionalWeather.isPresent());
        assertEquals(Double.valueOf(27), optionalWeather.get().getTemperature());

        verify(this.restTemplate, times(1)).exchange(anyString(), Mockito.any(), Mockito.any(),
                Mockito.<Class<HgWeather>>any());
    }

    @Test(expected = WeatherNotFoundException.class)
    public void shouldThrowWeatherNotFoundException() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.hgWeatherService.getWeather(weatherQuery);
    }

    @Test(expected = WeatherBadRequestException.class)
    public void shouldThrowWeatherBadRequestException() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        this.hgWeatherService.getWeather(weatherQuery);
    }

    @Test(expected = WeatherServiceUnavailableException.class)
    public void shouldThrowWeatherServiceUnavailableException() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.hgWeatherService.getWeather(weatherQuery);
    }

    @Test(expected = Exception.class)
    public void shouldThrowAnUnknownException() {
        final WeatherQuery weatherQuery = getWeatherQueryByLatitudeAndLongitude();

        when(this.restTemplate.exchange(anyString(), Mockito.any(), Mockito.any(), Mockito.<Class<HgWeather>>any()))
                .thenThrow(new IllegalArgumentException());

        this.hgWeatherService.getWeather(weatherQuery);
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