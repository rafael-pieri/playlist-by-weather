package com.playlistweather.model.music;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Track {

    @JsonProperty("name")
    private String name;

    @JsonProperty("durationMs")
    private Integer durationMs;

    public Track() {

    }

    public Track(final String name, final Integer durationMs) {
        this.name = name;
        this.durationMs = durationMs;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(final String name) {
        this.name = name;
    }

    @JsonProperty("durationMs")
    public Integer getDurationMs() {
        return durationMs;
    }

    @JsonProperty("durationMs")
    public void setDurationMs(final Integer durationMs) {
        this.durationMs = durationMs;
    }
}