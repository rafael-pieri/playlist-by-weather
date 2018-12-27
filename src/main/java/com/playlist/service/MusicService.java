package com.playlist.service;

import java.util.List;

import com.playlist.model.music.MusicQuery;
import com.playlist.model.music.Playlist;

public interface MusicService {

    List<Playlist> getTracksByCategory(final MusicQuery musicQuery);
}