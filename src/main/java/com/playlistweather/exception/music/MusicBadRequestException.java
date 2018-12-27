package com.playlistweather.exception.music;

import com.playlistweather.exception.BadRequestException;

public class MusicBadRequestException extends BadRequestException {

    public MusicBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}