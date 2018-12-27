package com.playlistweather.exception.weather;

import com.playlistweather.exception.NotFoundException;

public class WeatherNotFoundException extends NotFoundException {

    public WeatherNotFoundException(final String message) {
        super(message);
    }
}