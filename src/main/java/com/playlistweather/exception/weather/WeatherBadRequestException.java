package com.playlistweather.exception.weather;

import com.playlistweather.exception.BadRequestException;

public class WeatherBadRequestException extends BadRequestException {

    public WeatherBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}