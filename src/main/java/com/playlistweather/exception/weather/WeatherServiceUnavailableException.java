package com.playlistweather.exception.weather;

import com.playlistweather.exception.ServiceUnavailableException;

public class WeatherServiceUnavailableException extends ServiceUnavailableException {

    public WeatherServiceUnavailableException (final String message) {
        super(message);
    }

    public WeatherServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}