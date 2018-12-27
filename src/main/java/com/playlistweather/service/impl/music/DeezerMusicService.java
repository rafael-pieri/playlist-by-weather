package com.playlistweather.service.impl.music;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.playlistweather.exception.music.MusicBadRequestException;
import com.playlistweather.exception.music.MusicServiceUnavailableException;
import com.playlistweather.exception.music.PlaylistNotFoundException;
import com.playlistweather.model.music.MusicQuery;
import com.playlistweather.model.music.Playlist;
import com.playlistweather.model.music.Track;
import com.playlistweather.model.music.deezer.playlist.DeezerPlaylist;
import com.playlistweather.model.music.deezer.track.DeezerTrack;
import com.playlistweather.service.MusicService;

@Service
public class DeezerMusicService implements MusicService {

    private final Logger logger = LoggerFactory.getLogger(DeezerMusicService.class);

    private static final String QUERY_PARAM = "q";
    private static final String INDEX_PARAM = "index";
    private static final String LIMIT_PARAM = "limit";

    private final RestTemplate restTemplate;

    @Value("${deezer.url}")
    private String url;

    @Autowired
    public DeezerMusicService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Playlist> getTracksByCategory(final MusicQuery musicQuery) {
        logger.info("Calling Deezer music service, musicQuery={}", musicQuery);

        final String urlService = getUriComponentsBuilder(format("%s/search/playlistweather", this.url), musicQuery)
                .queryParam(QUERY_PARAM, musicQuery.getGenre())
                .toUriString();

        final DeezerPlaylist deezerPlaylist = callService(urlService, DeezerPlaylist.class);

        final List<Playlist> playlists = new ArrayList<>();

        deezerPlaylist.getData().forEach(playlist -> {
            final DeezerTrack deezerPlaylistDeezerTrack = callService(
                    getUriComponentsBuilder(playlist.getTracklist()).toUriString(),
                    DeezerTrack.class);

            final ArrayList<Track> tracks = new ArrayList<>();

            deezerPlaylistDeezerTrack.getData()
                    .forEach(track -> tracks.add(new Track(track.getTitle(), track.getDuration())));

            playlists.add(new Playlist(playlist.getTitle(), tracks));
        });

        return playlists;
    }

    private <T> T callService(final String urlService, final Class<T> responseType) {
        try {
            return this.restTemplate.getForEntity(urlService, responseType).getBody();

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                logger.error("Playlist not found, url={}, error={}", urlService, e.getMessage());
                throw new PlaylistNotFoundException("Playlist not found");
            }

            logger.error("Error when calling music service, url={}, error={}", urlService,
                    e.getMessage());
            throw new MusicBadRequestException("Error when calling music service", e);

        } catch (final HttpServerErrorException e) {
            logger.error("It was not possible to call music service, url={}, error={}", urlService,
                    e.getMessage());
            throw new MusicServiceUnavailableException("It was not possible to call music service", e);

        } catch (final Exception e) {
            logger.error("Unknown error, exception={}", e.getMessage());
            throw e;
        }
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String urlService, final MusicQuery musicQuery) {
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(urlService);

        if (musicQuery.getIndex() != null) {
            uriComponentsBuilder.queryParam(INDEX_PARAM, musicQuery.getIndex());
        }

        if (musicQuery.getLimit() != null) {
            uriComponentsBuilder.queryParam(LIMIT_PARAM, musicQuery.getLimit());
        }

        return uriComponentsBuilder;
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String urlService) {
        return UriComponentsBuilder.fromHttpUrl(urlService);
    }
}