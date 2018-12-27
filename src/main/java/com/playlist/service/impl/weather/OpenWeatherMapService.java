package com.playlist.service.impl.weather;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.playlist.exception.weather.WeatherBadRequestException;
import com.playlist.exception.weather.WeatherNotFoundException;
import com.playlist.exception.weather.WeatherServiceUnavailableException;
import com.playlist.model.weather.Weather;
import com.playlist.model.weather.WeatherQuery;
import com.playlist.model.weather.open.OpenWeather;
import com.playlist.service.WeatherService;

@Service
public class OpenWeatherMapService implements WeatherService {

    private final Logger logger = LoggerFactory.getLogger(OpenWeatherMapService.class);

    private static final String CITY_COUNTRY_ATTRIBUTE = "q";
    private static final String LATITUDE_ATTRIBUTE = "lat";
    private static final String LONGITUDE_ATTRIBUTE = "lon";
    private static final String APPID_ATTRIBUTE = "APPID";
    private static final String UNITS_ATTRIBUTE = "units";

    private final RestTemplate restTemplate;
    private final WeatherService hgWeatherService;

    @Value("${openWeatherMap.appId}")
    private String appId;

    @Value("${openWeatherMap.url}")
    private String url;

    @Value("${openWeatherMap.unit}")
    private String unit;

    @Autowired
    public OpenWeatherMapService(final RestTemplate restTemplate,
            @Qualifier("hgWeatherService") final WeatherService hgWeatherService) {
        this.restTemplate = restTemplate;
        this.hgWeatherService = hgWeatherService;
    }

    @Override
    @Cacheable(value = "get-weather", key = "#weatherQuery")
    @HystrixCommand(fallbackMethod = "getWeatherFallback", ignoreExceptions = { WeatherNotFoundException.class,
            WeatherBadRequestException.class })
    public Optional<Weather> getWeather(final WeatherQuery weatherQuery) {
        logger.info("Calling open weather map service, weatherQuery={}", weatherQuery);

        if (weatherQuery.isSearchByCityAndCountry()) {
            logger.info("Retrieving weather by city and country, city={}, country={}", weatherQuery.getNameCity(),
                    weatherQuery.getCountry());

            final OpenWeather openWeather = getByCityName(weatherQuery.getNameCity(),
                    weatherQuery.getCountry());
            return Optional.of(new Weather(openWeather.getMain().getTemp()));
        }

        if (weatherQuery.isSearchByLatitudeAndLongitude()) {
            logger.info("Retrieving weather by latitude and longitude, latitude={}, longitude={}",
                    weatherQuery.getLatitude(), weatherQuery.getLongitude());

            final OpenWeather openWeather = getByGeographicCoordination(
                    weatherQuery.getLatitude(), weatherQuery.getLongitude());
            return Optional.of(new Weather(openWeather.getMain().getTemp()));
        }

        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private Optional<Weather> getWeatherFallback(final WeatherQuery weatherQuery) {
        return hgWeatherService.getWeather(weatherQuery);
    }

    private OpenWeather getByCityName(final String nameCity, final String country) {
        final String urlService = getUriComponentsBuilder()
                .queryParam(CITY_COUNTRY_ATTRIBUTE, nameCity.concat(",").concat(country))
                .toUriString();
        return getWeatherDataLocation(urlService);
    }

    private OpenWeather getByGeographicCoordination(final BigDecimal latitude, final BigDecimal longitude) {
        final String urlService = getUriComponentsBuilder()
                .queryParam(LATITUDE_ATTRIBUTE, latitude)
                .queryParam(LONGITUDE_ATTRIBUTE, longitude)
                .toUriString();
        return getWeatherDataLocation(urlService);
    }

    private OpenWeather getWeatherDataLocation(final String urlService) {
        try {
            return this.restTemplate.getForEntity(urlService, OpenWeather.class).getBody();

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

    private UriComponentsBuilder getUriComponentsBuilder() {
        return UriComponentsBuilder.fromHttpUrl(this.url)
                .queryParam(APPID_ATTRIBUTE, this.appId)
                .queryParam(UNITS_ATTRIBUTE, this.unit);
    }
}