package com.playlistweather.service.impl.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.neovisionaries.i18n.CountryCode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.playlistweather.exception.music.MusicServiceUnavailableException;
import com.playlistweather.model.music.Playlist;
import com.playlistweather.model.music.MusicQuery;
import com.playlistweather.model.music.Track;
import com.playlistweather.service.MusicService;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

@Service
public class SpotifyMusicService implements MusicService {

    private final Logger logger = LoggerFactory.getLogger(SpotifyMusicService.class);

    private final SpotifyApi spotifyApi;
    private final MusicService deezerMusicService;

    @Autowired
    public SpotifyMusicService(final SpotifyApi spotifyApi,
            @Qualifier("deezerMusicService") final MusicService deezerMusicService) {
        this.spotifyApi = spotifyApi;
        this.deezerMusicService = deezerMusicService;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getTracksByCategoryFallback")
    public List<Playlist> getTracksByCategory(final MusicQuery musicQuery) {
        logger.info("Calling Spotify music service, musicQuery={}", musicQuery);

        final GetCategorysPlaylistsRequest getCategorysPlaylistRequest = getGetCategorysPlaylistRequest(musicQuery);

        try {
            final PlaylistSimplified[] playlistSimplifieds = getCategorysPlaylistRequest.execute().getItems();

            final List<Playlist> playlists = new ArrayList<>();

            for (final PlaylistSimplified playlistSimplified : playlistSimplifieds) {
                final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistSimplified.getId())
                        .market(CountryCode.BR)
                        .build();

                final PlaylistTrack[] tracks = getPlaylistRequest.execute().getTracks().getItems();

                final List<Track> trackList = Arrays.stream(tracks)
                        .map(track -> new Track(track.getTrack().getName(),
                                track.getTrack().getDurationMs()))
                        .collect(Collectors.toList());

                playlists.add(new Playlist(playlistSimplified.getName(), trackList));
            }

            return playlists;

        } catch (final IOException | SpotifyWebApiException e) {
            logger.info("It was not possible to call weather service, musicQuery={}, error={}", musicQuery,
                    e.getMessage());
            throw new MusicServiceUnavailableException("It was not possible to call weather service", e);

        } catch (final Exception e) {
            logger.error("Unknown error, exception={}", e.getMessage());
            throw e;
        }
    }

    private GetCategorysPlaylistsRequest getGetCategorysPlaylistRequest(final MusicQuery musicQuery) {
        final GetCategorysPlaylistsRequest.Builder builder = spotifyApi
                .getCategorysPlaylists(musicQuery.getGenre())
                .country(CountryCode.BR);

        if (musicQuery.getIndex() != null) {
            builder.offset(musicQuery.getIndex());
        }

        if (musicQuery.getLimit() != null) {
            builder.limit(musicQuery.getLimit());
        }

        return builder.build();
    }

    @SuppressWarnings("unused")
    private List<Playlist> getTracksByCategoryFallback(final MusicQuery musicQuery) {
        return deezerMusicService.getTracksByCategory(musicQuery);
    }
}