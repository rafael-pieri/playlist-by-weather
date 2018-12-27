package com.playlistweather.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException (final String message) {
        super(message);
    }

    public ServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}