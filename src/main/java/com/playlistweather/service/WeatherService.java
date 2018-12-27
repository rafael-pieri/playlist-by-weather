package com.playlistweather.service;

import java.util.Optional;

import com.playlistweather.model.weather.Weather;
import com.playlistweather.model.weather.WeatherQuery;

public interface WeatherService {

    Optional<Weather> getWeather(final WeatherQuery weatherQuery);
}