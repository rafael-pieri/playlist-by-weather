package com.playlist.exception.weather;

import com.playlist.exception.BadRequestException;

public class WeatherBadRequestException extends BadRequestException {

    public WeatherBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}