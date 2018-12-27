package com.playlistweather.model.weather;

import java.io.Serializable;

public class Weather implements Serializable {

    private static final long serialVersionUID = 9137074547063713310L;

    private final Double temperature;

    public Weather(final Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append("WeatherDTO [")
                .append("temperature=")
                .append(temperature)
                .append("]");
        return builder.toString();
    }
}