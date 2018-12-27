
package com.playlist.model.music.deezer.track;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeezerTrack {

    @JsonProperty("data")
    private List<DatumTrack> data = null;

    public DeezerTrack() {

    }

    public DeezerTrack(final List<DatumTrack> data) {
        this.data = data;
    }

    @JsonProperty("data")
    public List<DatumTrack> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(final List<DatumTrack> data) {
        this.data = data;
    }
}