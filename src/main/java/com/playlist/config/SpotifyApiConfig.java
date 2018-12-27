package com.playlist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.playlist.exception.music.SpotifyWebApiConfigException;
import com.wrapper.spotify.SpotifyApi;

@Configuration
public class SpotifyApiConfig {

    private final Logger logger = LoggerFactory.getLogger(SpotifyApiConfig.class);

    @Value("${spotifyApi.clientId}")
    private String clientId;

    @Value("${spotifyApi.clientSecret}")
    private String clientSecret;

    @Bean
    public SpotifyApi spotifyApi() {
        try {
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .build();

            final String accessToken = spotifyApi.clientCredentials()
                    .build()
                    .execute()
                    .getAccessToken();

            spotifyApi.setAccessToken(accessToken);

            return spotifyApi;

        } catch (final Exception e) {
            logger.error("Could not get spotify token, exception={}", e);
            throw new SpotifyWebApiConfigException("Could not get spotify token", e);
        }
    }
}