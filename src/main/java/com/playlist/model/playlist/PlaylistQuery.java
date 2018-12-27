package com.playlist.model.playlist;

import java.math.BigDecimal;

import com.playlist.exception.playlist.PlaylistQueryException;

public class PlaylistQuery {

    private String nameCity;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer index;
    private Integer limit;

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

    public Integer getIndex() {
        return index;
    }

    public Integer getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append("PlaylistQuery [")
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

        private final PlaylistQuery playlistQuery = new PlaylistQuery();

        public Builder withNameCity(final String nameCity) {
            this.playlistQuery.nameCity = nameCity;
            return this;
        }

        public Builder withCountry(final String country) {
            this.playlistQuery.country = country;
            return this;
        }

        public Builder withLatitude(final BigDecimal latitude) {
            this.playlistQuery.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final BigDecimal longitude) {
            this.playlistQuery.longitude = longitude;
            return this;
        }

        public Builder withIndex(final Integer index) {
            this.playlistQuery.index = index;
            return this;
        }

        public Builder withLimit(final Integer limit) {
            this.playlistQuery.limit = limit;
            return this;
        }

        public PlaylistQuery build() {
            if (playlistQuery.nameCity != null && playlistQuery.country != null ||
                    playlistQuery.latitude != null && playlistQuery.longitude != null) {
                return this.playlistQuery;
            }

            throw new PlaylistQueryException(
                    "Invalid search parameters. Please enter city and country, or latitude and longitude.");
        }
    }
}