package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RequiredArgsConstructor
public class StatsClientTest {
    private final String HOST = "http://localhost:";
    private final String PORT = "9090";
    private final RestTemplate restTemplate = new RestTemplate();
    private final StatsClient client = new StatsClient(restTemplate);
    private final MockRestServiceServer mockStatsService = MockRestServiceServer.bindTo(restTemplate)
            .build();

    @Test
    @SneakyThrows
    public void postEndpointHitTest() {
        LocalDateTime now = LocalDateTime.now();
        mockStatsService.expect(requestTo(HOST + PORT + "/hit"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.app").value("app"))
                .andExpect(jsonPath("$.uri").value("/events"))
                .andExpect(jsonPath("$.ip").value("127.110.68.30"))
                .andExpect(jsonPath("$.timestamp").value(
                        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        ))
                .andRespond(withSuccess());

        client.postHit("app", "/events", "127.110.68.30", now);

        mockStatsService.verify();
    }

    @Test
    @SneakyThrows
    public void getStatsTest() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        mockStatsService.expect(
                requestTo(HOST + PORT + "/stats"
                        + "?start=" + start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss"))
                        + "&end=" + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss"))
                        + "&unique=true")
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(generateViewStatsResponse(), MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = client.getStats(start, end, List.of(), true);

        mockStatsService.verify();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(generateViewStatsResponse(), "\"" + response.getBody() + "\"");
    }

    private String generateViewStatsResponse() throws JsonProcessingException {
        ViewStats viewStats1 = ViewStats.builder()
                .app("app1")
                .uri("uri1")
                .hits(7L)
                .build();
        ViewStats viewStats2 = ViewStats.builder()
                .app("app2")
                .uri("uri2")
                .hits(10L)
                .build();
        ViewStats viewStats3 = ViewStats.builder()
                .app("app3")
                .uri("uri3")
                .hits(3L)
                .build();
        return new ObjectMapper().writeValueAsString(List.of(viewStats1, viewStats2, viewStats3).toString());
    }
}
