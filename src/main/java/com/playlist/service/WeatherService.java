package com.playlist.service;

import java.util.Optional;

import com.playlist.model.weather.Weather;
import com.playlist.model.weather.WeatherQuery;

public interface WeatherService {

    Optional<Weather> getWeather(final WeatherQuery weatherQuery);
}