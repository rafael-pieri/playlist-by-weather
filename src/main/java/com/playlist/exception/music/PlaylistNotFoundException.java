package com.playlist.exception.music;

import com.playlist.exception.NotFoundException;

public class PlaylistNotFoundException extends NotFoundException {

    public PlaylistNotFoundException(final String message) {
        super(message);
    }
}