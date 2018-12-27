package com.playlistweather.exception.music;

import com.playlistweather.exception.ServiceUnavailableException;

public class MusicServiceUnavailableException extends ServiceUnavailableException {

    public MusicServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}