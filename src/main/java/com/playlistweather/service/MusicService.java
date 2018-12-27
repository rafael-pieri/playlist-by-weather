package com.playlistweather.service;

import java.util.List;

import com.playlistweather.model.music.MusicQuery;
import com.playlistweather.model.music.Playlist;

public interface MusicService {

    List<Playlist> getTracksByCategory(final MusicQuery musicQuery);
}