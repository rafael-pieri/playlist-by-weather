package com.playlistweather.model.weather;

import java.io.Serializable;
import java.math.BigDecimal;

public class WeatherQuery implements Serializable {

    private static final long serialVersionUID = -4937544935469259175L;

    private String nameCity;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public String getNameCity() {
        return nameCity;
    }

    public String getCountry() {
        return country;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public boolean isSearchByCityAndCountry() {
        return nameCity != null && country != null;
    }

    public boolean isSearchByLatitudeAndLongitude() {
        return latitude != null && longitude != null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append("WeatherQuery [")
                .append("nameCity=\"")
                .append(nameCity).append("\"")
                .append(",country=\"")
                .append(country).append("\"")
                .append(",latitude=")
                .append(latitude)
                .append(",longitude=")
                .append(longitude)
                .append("]");
        return builder.toString();
    }

    public static class Builder {

        private final WeatherQuery weatherQuery = new WeatherQuery();

        public Builder withNameCity(final String nameCity) {
            this.weatherQuery.nameCity = nameCity;
            return this;
        }

        public Builder withCountry(final String country) {
            this.weatherQuery.country = country;
            return this;
        }

        public Builder withLatitude(final BigDecimal latitude) {
            this.weatherQuery.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final BigDecimal longitude) {
            this.weatherQuery.longitude = longitude;
            return this;
        }

        public WeatherQuery build() {
            return this.weatherQuery;
        }
    }
}