package com.playlist.controller;

import java.math.BigDecimal;

import com.playlist.model.playlist.PlaylistResponse;
import com.playlist.model.playlist.PlaylistQuery;
import com.playlist.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(final PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public PlaylistResponse getPlaylist(@RequestParam(value = "nameCity", required = false) final String nameCity,
            @RequestParam(value = "country", required = false) final String country,
            @RequestParam(value = "latitude", required = false) final BigDecimal latitude,
            @RequestParam(value = "longitude", required = false) final BigDecimal longitude,
            @RequestParam(value = "index", required = false, defaultValue = "0") final Integer index,
            @RequestParam(value = "limit", required = false, defaultValue = "5") final Integer limit) {

        final PlaylistQuery playlistQuery = new PlaylistQuery.Builder()
                .withNameCity(nameCity)
                .withCountry(country)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .withIndex(index)
                .withLimit(limit)
                .build();

        return this.playlistService.getPlaylist(playlistQuery);
    }
}