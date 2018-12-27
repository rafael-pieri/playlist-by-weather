
package com.playlistweather.model.music.deezer.track;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatumTrack {

    @JsonProperty("title")
    private String title;

    @JsonProperty("duration")
    private Integer duration;

    public DatumTrack() {

    }

    public DatumTrack(final String title, final Integer duration) {
        this.title = title;
        this.duration = duration;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(final String title) {
        this.title = title;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
}