package ru.practicum.ewm;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import ru.practicum.client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class EwmApp {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(EwmApp.class, args);
        StatsClient client = new StatsClient(new RestTemplate());
        client.postHit("app", "/events/1", "125.04.12.02", LocalDateTime.now());
        client.postHit("app", "/events/1", "125.04.12.02", LocalDateTime.now());
        client.postHit("app", "/events/1", "126.04.12.02", LocalDateTime.now());
        client.postHit("app", "/events/2", "126.04.12.02", LocalDateTime.now());
        System.out.println(
                "client.getStats() = " + client.getStats(
                        LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                        LocalDateTime.of(2025, 10, 10, 10, 10, 10),
                        List.of("/events/1"),
                        true
                ).getBody()
        );
        System.out.println(
                "client.getStats() = " + client.getStats(
                        LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                        LocalDateTime.of(2025, 10, 10, 10, 10, 10),
                        List.of("/events/1"),
                        false
                ).getBody()
        );
        System.out.println(
                "client.getStats() = " + client.getStats(
                        LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                        LocalDateTime.of(2025, 10, 10, 10, 10, 10),
                        List.of("/events/2"),
                        false
                ).getBody()
        );
    }
}