package com.playlist.service.impl.weather;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.playlist.exception.weather.WeatherBadRequestException;
import com.playlist.exception.weather.WeatherNotFoundException;
import com.playlist.exception.weather.WeatherServiceUnavailableException;
import com.playlist.model.weather.Weather;
import com.playlist.model.weather.WeatherQuery;
import com.playlist.model.weather.hg.HgWeather;
import com.playlist.service.WeatherService;

@Service
public class HgWeatherService implements WeatherService {

    private final Logger logger = LoggerFactory.getLogger(HgWeatherService.class);

    private static final String CITY_NAME_PARAM = "city_name";
    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "lon";
    private static final String KEY_PARAM = "key";
    private static final String USER_IP_PARAM = "user_ip";
    private static final String USER_IP_VALUE = "remote";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String USER_AGENT_HEADER = "user-agent";

    private final RestTemplate restTemplate;

    @Value("${hgBrasil.id}")
    private String appId;

    @Value("${hgBrasil.url}")
    private String url;

    @Value("${hgBrasil.headerRequest.userAgent}")
    private String userAgent;

    @Autowired
    public HgWeatherService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "get-weather", key = "#weatherQuery")
    public Optional<Weather> getWeather(final WeatherQuery weatherQuery) {
        logger.info("Calling HG weather service, weatherQuery={}", weatherQuery);

        if (weatherQuery.isSearchByCityAndCountry()) {
            logger.info("Retrieving weather by city and country, weatherQuery={}", weatherQuery);

            final HgWeather hgWeather = getByCityName(weatherQuery.getNameCity(),
                    weatherQuery.getCountry());
            return Optional.of(new Weather(Double.valueOf(hgWeather.getResults().getTemp())));
        }

        if (weatherQuery.isSearchByLatitudeAndLongitude()) {
            logger.info("Retrieving weather by latitude and longitude, weatherQuery={}", weatherQuery);

            final HgWeather hgWeather = getByGeographicCoordination(
                    weatherQuery.getLatitude(), weatherQuery.getLongitude());
            return Optional.of(new Weather(Double.valueOf(hgWeather.getResults().getTemp())));
        }

        return Optional.empty();
    }

    private HgWeather getByCityName(final String nameCity, final String country) {
        final String urlService = getUriComponentsBuilder()
                .queryParam(CITY_NAME_PARAM, nameCity.concat(",").concat(country))
                .toUriString();
        return getHgWeatherDTO(urlService);
    }

    private HgWeather getByGeographicCoordination(final BigDecimal latitude, final BigDecimal longitude) {
        final String urlService = getUriComponentsBuilder()
                .queryParam(LATITUDE_PARAM, latitude)
                .queryParam(LONGITUDE_PARAM, longitude)
                .queryParam(USER_IP_PARAM, USER_IP_VALUE)
                .toUriString();
        return getHgWeatherDTO(urlService);
    }

    private UriComponentsBuilder getUriComponentsBuilder() {
        return UriComponentsBuilder.fromHttpUrl(this.url)
                .queryParam(KEY_PARAM, this.appId);
    }

    private HgWeather getHgWeatherDTO(final String urlService) {
        try {
            return this.restTemplate.exchange(urlService, HttpMethod.GET, getDefaultHeaders(), HgWeather.class)
                    .getBody();

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                logger.error("Weather not found, url={}, error={}", urlService, e.getMessage());
                throw new WeatherNotFoundException("Weather not found");
            }

            logger.error("Error when calling weather service, url={}, error={}", urlService,
                    e.getMessage());
            throw new WeatherBadRequestException("Error when calling weather service", e);

        } catch (final HttpServerErrorException e) {
            logger.error("It was not possible to call weather service, url={}, error={}", urlService,
                    e.getMessage());
            throw new WeatherServiceUnavailableException("It was not possible to call weather service", e);

        } catch (final Exception e) {
            logger.error("Unknown error, exception={}", e.getMessage());
            throw e;
        }
    }

    private HttpEntity<?> getDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
        headers.add(USER_AGENT_HEADER, this.userAgent);
        return new HttpEntity<>(headers);
    }
}