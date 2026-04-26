package com.example.starwars_facade;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/swapi")
@Slf4j
@RequiredArgsConstructor
public class StarwarsController {

    private final RestTemplate restTemplate;
    private static final String SWAPI_BASE_URL = "https://swapi.dev/api/people/";

    @GetMapping("/character/{id}")
    @CircuitBreaker(name = "swapi", fallbackMethod = "fallbackCharacter")
    @RateLimiter(name = "swapi")
    @Retry(name = "swapi")
    public Map<String, Object> getCharacter(@PathVariable int id) {
        log.info(">>>> FACADE: Calling SWAPI for character ID {}...", id);
        return restTemplate.getForObject(SWAPI_BASE_URL + id + "/", Map.class);
    }

    // Fallback method when SWAPI is down or circuit is open
    public Map<String, Object> fallbackCharacter(int id, Throwable t) {
        log.error(">>>> FACADE FALLBACK: SWAPI unreachable or rate limited. Error: {}", t.getMessage());
        return Map.of(
            "name", "Unknown Hero (Fallback)",
            "status", "SWAPI_UNAVAILABLE"
        );
    }
}
