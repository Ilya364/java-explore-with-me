package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class StatsClient {
    private final RestTemplate rest;
    private static final String POST_HIT_PATH = "http://localhost:9090/hit";
    private static final String GET_STATS_PATH = "http://localhost:9090/stats";

    public StatsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public ResponseEntity<Object> postHit(
            String app, String uri, String ip, LocalDateTime timestamp
    ) throws JsonProcessingException {
        EndpointHit endpointHit = buildEndpointHit(app, uri, ip, timestamp);
        String body = buildBody(endpointHit);
        return makeAndSendRequest(HttpMethod.POST, POST_HIT_PATH, null, body);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = buildParameters(start, end, uris, unique);
        return makeAndSendRequest(
                HttpMethod.GET,
                GET_STATS_PATH + "?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters,
                null);
    }

    private EndpointHit buildEndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        return EndpointHit.builder()
            .app(app)
            .uri(uri)
            .ip(ip)
            .hitTimestamp(timestamp)
            .build();
    }

    private String buildBody(EndpointHit endpointHit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(endpointHit);
    }

    private Map<String, Object> buildParameters(
            LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique
    ) {
        String startStr = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endStr = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Map.of(
                "start", startStr,
                "end", endStr,
                "uris", uris,
                "unique", unique
                );
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(
            HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body
    ) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
