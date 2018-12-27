package com.playlistweather.model.music;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Playlist {

    @JsonProperty("name")
    private String name;

    @JsonProperty("tracks")
    private List<Track> tracks;

    public Playlist () {

    }

    public Playlist (final String name, final List<Track> tracks) {
        this.name = name;
        this.tracks = tracks;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(final String name) {
        this.name = name;
    }

    @JsonProperty("tracks")
    public List<Track> getTracks() {
        return tracks;
    }

    @JsonProperty("tracks")
    public void setTracks(final List<Track> tracks) {
        this.tracks = tracks;
    }
}