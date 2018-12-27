package com.playlist.model.music;

public class MusicQuery {

    private String genre;
    private Integer index;
    private Integer limit;

    public String getGenre() {
        return genre;
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
                .append("MusicQuery [")
                .append("genre=\"")
                .append(genre).append("\"")
                .append(",index=")
                .append(index)
                .append(",limit=")
                .append(limit)
                .append("]");
        return builder.toString();
    }

    public static class Builder {

        private final MusicQuery musicQuery = new MusicQuery();

        public Builder withGenre(final String genre) {
            this.musicQuery.genre = genre;
            return this;
        }

        public Builder withIndex(final Integer index) {
            this.musicQuery.index = index;
            return this;
        }

        public Builder withLimit(final Integer limit) {
            this.musicQuery.limit = limit;
            return this;
        }

        public MusicQuery build() {
            return musicQuery;
        }
    }
}