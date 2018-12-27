package com.playlistweather.exception.music;

import com.playlistweather.exception.NotFoundException;

public class PlaylistNotFoundException extends NotFoundException {

    public PlaylistNotFoundException(final String message) {
        super(message);
    }
}