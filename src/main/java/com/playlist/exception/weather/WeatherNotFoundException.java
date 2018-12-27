package com.playlist.exception.weather;

import com.playlist.exception.NotFoundException;

public class WeatherNotFoundException extends NotFoundException {

    public WeatherNotFoundException(final String message) {
        super(message);
    }
}