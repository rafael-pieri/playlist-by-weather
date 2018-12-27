
package com.playlist.model.weather.hg;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Results {

    @JsonProperty("temp")
    private Integer temp;

    public Results() {

    }

    public Results(final Integer temp) {
        this.temp = temp;
    }

    @JsonProperty("temp")
    public Integer getTemp() {
        return temp;
    }

    @JsonProperty("temp")
    public void setTemp(final Integer temp) {
        this.temp = temp;
    }
}