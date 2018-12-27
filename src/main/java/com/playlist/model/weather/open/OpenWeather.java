
package com.playlist.model.weather.open;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenWeather {

    @JsonProperty("main")
    private Main main;

    public OpenWeather() {

    }

    public OpenWeather(final Main main) {
        this.main = main;
    }

    @JsonProperty("main")
    public Main getMain() {
        return main;
    }

    @JsonProperty("main")
    public void setMain(final Main main) {
        this.main = main;
    }
}