package com.playlist.exception.playlist;

import com.playlist.exception.BadRequestException;

public class PlaylistQueryException extends BadRequestException {

    public PlaylistQueryException(final String message) {
        super(message);
    }
}