package com.playlistweather.service;

import com.playlistweather.model.playlist.PlaylistResponse;
import com.playlistweather.model.playlist.PlaylistQuery;

public interface PlaylistService {

    PlaylistResponse getPlaylist(final PlaylistQuery playlistQuery);
}