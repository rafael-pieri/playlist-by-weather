package com.playlist.exception.music;

import com.playlist.exception.BadRequestException;

public class MusicBadRequestException extends BadRequestException {

    public MusicBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}