
package com.playlistweather.model.weather.hg;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HgWeather {

    @JsonProperty("results")
    private Results results;

    public HgWeather() {

    }

    public HgWeather(final Results results) {
        this.results = results;
    }

    @JsonProperty("results")
    public Results getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(final Results results) {
        this.results = results;
    }
}