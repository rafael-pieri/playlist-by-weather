package com.playlistweather.service.music;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.playlistweather.exception.music.MusicBadRequestException;
import com.playlistweather.exception.music.MusicServiceUnavailableException;
import com.playlistweather.exception.music.PlaylistNotFoundException;
import com.playlistweather.model.music.MusicQuery;
import com.playlistweather.model.music.Playlist;
import com.playlistweather.model.music.deezer.playlist.DatumPlaylist;
import com.playlistweather.model.music.deezer.playlist.DeezerPlaylist;
import com.playlistweather.model.music.deezer.track.DatumTrack;
import com.playlistweather.model.music.deezer.track.DeezerTrack;
import com.playlistweather.model.playlist.enums.PlaylistGenre;
import com.playlistweather.service.impl.music.DeezerMusicService;

@RunWith(MockitoJUnitRunner.class)
public class DeezerMusicServiceTest {

    private static final String PLAYLIST_NAME = "Any Playlist Name";
    private static final String TRACK_NAME = "Any Track Name";
    private static final String TRACK_LIST_URL = "https://api.deezer.com/playlistweather/4590825924/tracks";
    private static final String PLAYLIST_URL = "https://api.deezer.com/search/playlistweather?index=0&limit=10&q=party";
    private static final int INDEX = 0;
    private static final int LIMIT = 10;
    private static final String DEEZER_URL = "https://api.deezer.com";
    private static final String URL_ATTRIBUTE = "url";

    @InjectMocks
    private DeezerMusicService deezerMusicService;

    @Mock
    private RestTemplate restTemplate;

    private MusicQuery musicQuery;

    @Before
    public void setup() {
        this.musicQuery = new MusicQuery.Builder()
                .withGenre(PlaylistGenre.PARTY.description())
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();
    }

    @Test
    public void shouldGetPlaylist() {
        ReflectionTestUtils.setField(this.deezerMusicService, URL_ATTRIBUTE, DEEZER_URL);

        when(this.restTemplate.getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any()))
                .thenReturn(new ResponseEntity<>(getDeezerPlaylist(), HttpStatus.OK));

        when(this.restTemplate.getForEntity(eq(TRACK_LIST_URL),
                Mockito.<Class<DeezerTrack>>any())).thenReturn(new ResponseEntity<>(getDeezerTrack(), HttpStatus.OK));

        final Playlist playlists = this.deezerMusicService.getTracksByCategory(this.musicQuery).get(0);

        assertEquals(PLAYLIST_NAME, playlists.getName());
        assertEquals(TRACK_NAME, playlists.getTracks().get(0).getName());
        assertEquals(Integer.valueOf(3000), playlists.getTracks().get(0).getDurationMs());

        verify(this.restTemplate, times(1)).getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any());
        verify(this.restTemplate, times(1)).getForEntity(eq(TRACK_LIST_URL), Mockito.<Class<DeezerTrack>>any());
    }

    @Test(expected = PlaylistNotFoundException.class)
    public void shouldThrowPlaylistNotFoundException() {
        ReflectionTestUtils.setField(deezerMusicService, URL_ATTRIBUTE, DEEZER_URL);

        when(this.restTemplate.getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.deezerMusicService.getTracksByCategory(this.musicQuery);
    }

    @Test(expected = MusicBadRequestException.class)
    public void shouldThrowMusicBadRequestException() {
        ReflectionTestUtils.setField(deezerMusicService, URL_ATTRIBUTE, DEEZER_URL);

        when(this.restTemplate.getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        this.deezerMusicService.getTracksByCategory(this.musicQuery);
    }

    @Test(expected = MusicServiceUnavailableException.class)
    public void shouldThrowMusicServiceUnavailableException() {
        ReflectionTestUtils.setField(deezerMusicService, URL_ATTRIBUTE, DEEZER_URL);

        when(this.restTemplate.getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any()))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.deezerMusicService.getTracksByCategory(this.musicQuery);
    }

    @Test(expected = Exception.class)
    public void shouldThrowAnUnknownException() {
        ReflectionTestUtils.setField(deezerMusicService, URL_ATTRIBUTE, DEEZER_URL);

        when(this.restTemplate.getForEntity(eq(PLAYLIST_URL), Mockito.<Class<DeezerPlaylist>>any()))
                .thenThrow(new IllegalArgumentException());

        this.deezerMusicService.getTracksByCategory(this.musicQuery);
    }

    private DeezerTrack getDeezerTrack() {
        final DatumTrack datumTrack = new DatumTrack(TRACK_NAME, 3000);
        return new DeezerTrack(Collections.singletonList(datumTrack));
    }

    private DeezerPlaylist getDeezerPlaylist() {
        final DatumPlaylist datumPlaylist = new DatumPlaylist(PLAYLIST_NAME, TRACK_LIST_URL);
        return new DeezerPlaylist(Collections.singletonList(datumPlaylist));
    }
}