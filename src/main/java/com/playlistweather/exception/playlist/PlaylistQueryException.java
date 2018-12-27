package com.playlistweather.exception.playlist;

import com.playlistweather.exception.BadRequestException;

public class PlaylistQueryException extends BadRequestException {

    public PlaylistQueryException(final String message) {
        super(message);
    }
}