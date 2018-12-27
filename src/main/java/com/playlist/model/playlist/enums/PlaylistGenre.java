package com.playlist.model.playlist.enums;

/**
 * @author s2it_rbarbosa
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 21/12/18 13:13
 */
public enum PlaylistGenre {

    PARTY("party"), POP("pop"), ROCK("rock"), CLASSICAL("classical");

    private final String url;

    PlaylistGenre(final String url) {
        this.url = url;
    }

    public String description() {
        return url;
    }
}
