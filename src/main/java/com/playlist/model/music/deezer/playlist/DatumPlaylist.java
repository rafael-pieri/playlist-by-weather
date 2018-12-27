
package com.playlist.model.music.deezer.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatumPlaylist {

    @JsonProperty("title")
    private String title;

    @JsonProperty("tracklist")
    private String tracklist;

    public DatumPlaylist() {

    }

    public DatumPlaylist(final String title, final String tracklist) {
        this.title = title;
        this.tracklist = tracklist;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(final String title) {
        this.title = title;
    }

    @JsonProperty("tracklist")
    public String getTracklist() {
        return tracklist;
    }

    @JsonProperty("tracklist")
    public void setTracklist(final String tracklist) {
        this.tracklist = tracklist;
    }
}