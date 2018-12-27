package com.playlist.exception.music;

import com.playlist.exception.ServiceUnavailableException;

public class MusicServiceUnavailableException extends ServiceUnavailableException {

    public MusicServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}