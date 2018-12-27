package com.playlist.service;

import com.playlist.model.playlist.PlaylistResponse;
import com.playlist.model.playlist.PlaylistQuery;

public interface PlaylistService {

    PlaylistResponse getPlaylist(final PlaylistQuery playlistQuery);
}