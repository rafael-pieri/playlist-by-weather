package com.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
@EnableCaching
public class PlaylistWeatherApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PlaylistWeatherApplication.class, args);
    }
}