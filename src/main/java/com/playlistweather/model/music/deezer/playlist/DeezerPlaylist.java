
package com.playlistweather.model.music.deezer.playlist;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeezerPlaylist {

    @JsonProperty("data")
    private List<DatumPlaylist> data = null;

    public DeezerPlaylist() {

    }

    public DeezerPlaylist(final List<DatumPlaylist> data) {
        this.data = data;
    }

    @JsonProperty("data")
    public List<DatumPlaylist> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(final List<DatumPlaylist> data) {
        this.data = data;
    }
}