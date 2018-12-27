package com.playlistweather.service.impl.playlist;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.playlistweather.exception.weather.WeatherNotFoundException;
import com.playlistweather.model.music.Playlist;
import com.playlistweather.model.music.MusicQuery;
import com.playlistweather.model.playlist.PlaylistResponse;
import com.playlistweather.model.playlist.PlaylistQuery;
import com.playlistweather.model.playlist.enums.PlaylistGenre;
import com.playlistweather.model.weather.Weather;
import com.playlistweather.model.weather.WeatherQuery;
import com.playlistweather.service.MusicService;
import com.playlistweather.service.PlaylistService;
import com.playlistweather.service.WeatherService;

@Service
public class SmartPlaylistService implements PlaylistService {

    private final Logger logger = LoggerFactory.getLogger(SmartPlaylistService.class);

    private final WeatherService weatherService;
    private final MusicService musicService;

    @Autowired
    public SmartPlaylistService(@Qualifier("openWeatherMapService") final WeatherService weatherService,
            @Qualifier("spotifyMusicService") final MusicService musicService) {
        this.weatherService = weatherService;
        this.musicService = musicService;
    }

    public PlaylistResponse getPlaylist(final PlaylistQuery playlistQuery) {
        logger.info("Calling smart playlistweather service, playlistQuery={}", playlistQuery);

        final WeatherQuery weatherQuery = new WeatherQuery.Builder()
                .withNameCity(playlistQuery.getNameCity())
                .withCountry(playlistQuery.getCountry())
                .withLatitude(playlistQuery.getLatitude())
                .withLongitude(playlistQuery.getLongitude())
                .build();

        final Optional<Weather> optionalWeather = weatherService.getWeather(weatherQuery);

        if (optionalWeather.isPresent()) {
            final MusicQuery musicQuery = new MusicQuery.Builder()
                    .withGenre(getPlaylistGenre(optionalWeather.get().getTemperature()))
                    .withIndex(playlistQuery.getIndex())
                    .withLimit(playlistQuery.getLimit())
                    .build();

            final List<Playlist> playlists = musicService.getTracksByCategory(musicQuery);

            return new PlaylistResponse(playlists);
        }

        logger.info("Weather not found, playlistQuery={}", playlistQuery);

        throw new WeatherNotFoundException("Weather not found");
    }

    private String getPlaylistGenre(final Double temperature) {
        if (temperature > 30) {
            return PlaylistGenre.PARTY.description();
        } else if (temperature <= 30 && temperature >= 15) {
            return PlaylistGenre.POP.description();
        } else if (temperature < 15 && temperature >= 10) {
            return PlaylistGenre.ROCK.description();
        } else {
            return PlaylistGenre.CLASSICAL.description();
        }
    }
}