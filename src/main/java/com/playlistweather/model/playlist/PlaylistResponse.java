package com.playlistweather.model.playlist;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playlistweather.model.music.Playlist;

public class PlaylistResponse {

    @JsonProperty("playlists")
    private List<Playlist> playlists;

    public PlaylistResponse() {

    }

    public PlaylistResponse(final List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @JsonProperty("playlists")
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    @JsonProperty("playlists")
    public void setPlaylists(final List<Playlist> playlists) {
        this.playlists = playlists;
    }
}