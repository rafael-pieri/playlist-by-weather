package com.playlistweather.service.playlist;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.playlistweather.exception.weather.WeatherNotFoundException;
import com.playlistweather.model.music.Playlist;
import com.playlistweather.model.music.Track;
import com.playlistweather.model.playlist.PlaylistQuery;
import com.playlistweather.model.playlist.PlaylistResponse;
import com.playlistweather.model.playlist.enums.PlaylistGenre;
import com.playlistweather.model.weather.Weather;
import com.playlistweather.service.MusicService;
import com.playlistweather.service.WeatherService;
import com.playlistweather.service.impl.playlist.SmartPlaylistService;

@RunWith(MockitoJUnitRunner.class)
public class SmartPlaylistResponseServiceTest {

    private static final String TRACK_NAME = "Any Title Name";
    private static final String AMERICANA = "Americana";
    private static final String BRAZIL = "br";
    private static final int INDEX = 0;
    private static final int LIMIT = 10;
    private static final String WEATHER_NOT_FOUND = "Weather not found";

    @InjectMocks
    private SmartPlaylistService smartPlaylistService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private MusicService musicService;

    @Test
    public void shouldGetPartyPlaylistByLatitudeAndLongitude() {
        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withLatitude(BigDecimal.valueOf(-12.7))
                .withLongitude(BigDecimal.valueOf(8.9))
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();

        final List<Track> tracks = Collections.singletonList(new Track(TRACK_NAME, 3000));
        final List<Playlist> playlists = Collections
                .singletonList(new Playlist(PlaylistGenre.PARTY.description(), tracks));

        when(this.weatherService.getWeather(any())).thenReturn(Optional.of(new Weather(31.0)));
        when(this.musicService.getTracksByCategory(any())).thenReturn(playlists);

        final PlaylistResponse musicPlaylistResult = this.smartPlaylistService.getPlaylist(playlistQuery);

        final Playlist playlist = musicPlaylistResult.getPlaylists().get(0);

        assertEquals(PlaylistGenre.PARTY.description(), playlist.getName());
        assertEquals(TRACK_NAME, playlist.getTracks().get(0).getName());
        assertEquals(Integer.valueOf(3000), playlist.getTracks().get(0).getDurationMs());

        verify(this.weatherService, times(1)).getWeather(any());
        verify(this.musicService, times(1)).getTracksByCategory(any());
    }

    @Test
    public void shouldGetPopPlaylistByCityAndCountry() {
        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withNameCity(AMERICANA)
                .withCountry(BRAZIL)
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();

        final List<Track> tracks = Collections.singletonList(new Track(TRACK_NAME, 3000));
        final List<Playlist> playlists = Collections
                .singletonList(new Playlist(PlaylistGenre.POP.description(), tracks));

        when(this.weatherService.getWeather(any())).thenReturn(Optional.of(new Weather(21.0)));
        when(this.musicService.getTracksByCategory(any())).thenReturn(playlists);

        final PlaylistResponse musicPlaylistResult = this.smartPlaylistService.getPlaylist(playlistQuery);

        final Playlist playlist = musicPlaylistResult.getPlaylists().get(0);

        assertEquals(PlaylistGenre.POP.description(), playlist.getName());
        assertEquals(TRACK_NAME, playlist.getTracks().get(0).getName());
        assertEquals(Integer.valueOf(3000), playlist.getTracks().get(0).getDurationMs());

        verify(this.weatherService, times(1)).getWeather(any());
        verify(this.musicService, times(1)).getTracksByCategory(any());
    }

    @Test
    public void shouldGetRockPlaylistByCityAndCountry() {
        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withNameCity(AMERICANA)
                .withCountry(BRAZIL)
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();

        final List<Track> tracks = Collections.singletonList(new Track(TRACK_NAME, 3000));
        final List<Playlist> playlists = Collections
                .singletonList(new Playlist(PlaylistGenre.ROCK.description(), tracks));

        when(this.weatherService.getWeather(any())).thenReturn(Optional.of(new Weather(14.0)));
        when(this.musicService.getTracksByCategory(any())).thenReturn(playlists);

        final PlaylistResponse musicPlaylistResult = this.smartPlaylistService.getPlaylist(playlistQuery);

        final Playlist playlist = musicPlaylistResult.getPlaylists().get(0);

        assertEquals(PlaylistGenre.ROCK.description(), playlist.getName());
        assertEquals(TRACK_NAME, playlist.getTracks().get(0).getName());
        assertEquals(Integer.valueOf(3000), playlist.getTracks().get(0).getDurationMs());

        verify(this.weatherService, times(1)).getWeather(any());
        verify(this.musicService, times(1)).getTracksByCategory(any());
    }

    @Test
    public void shouldGetClassicalPlaylistByLatitudeAndLongitude() {
        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withLatitude(BigDecimal.valueOf(-12.7))
                .withLongitude(BigDecimal.valueOf(8.9))
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();

        final List<Track> tracks = Collections.singletonList(new Track(TRACK_NAME, 3000));
        final List<Playlist> playlists = Collections
                .singletonList(new Playlist(PlaylistGenre.CLASSICAL.description(), tracks));

        when(this.weatherService.getWeather(any())).thenReturn(Optional.of(new Weather(9.0)));
        when(this.musicService.getTracksByCategory(any())).thenReturn(playlists);

        final PlaylistResponse musicPlaylistResult = this.smartPlaylistService.getPlaylist(playlistQuery);

        final Playlist playlist = musicPlaylistResult.getPlaylists().get(0);

        assertEquals(PlaylistGenre.CLASSICAL.description(), playlist.getName());
        assertEquals(TRACK_NAME, playlist.getTracks().get(0).getName());
        assertEquals(Integer.valueOf(3000), playlist.getTracks().get(0).getDurationMs());

        verify(this.weatherService, times(1)).getWeather(any());
        verify(this.musicService, times(1)).getTracksByCategory(any());
    }

    @Test(expected = WeatherNotFoundException.class)
    public void shouldThrowAnExceptionWhenWeatherIsNotFound() {
        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withLatitude(BigDecimal.valueOf(-12.7))
                .withLongitude(BigDecimal.valueOf(8.9))
                .withIndex(INDEX)
                .withLimit(LIMIT)
                .build();

        when(this.weatherService.getWeather(any())).thenThrow(new WeatherNotFoundException(WEATHER_NOT_FOUND));

        this.smartPlaylistService.getPlaylist(playlistQuery);
    }
}