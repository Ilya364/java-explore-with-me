package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate rest;
    //@Value("${datetime.pattern}")
    private String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    //@Value("${stats.service.host}")
    private final String HOST = "localhost";
    //@Value("${stats.service.port}")
    private final String PORT = "9090";
    private final String POST_HIT_PATH = "http://" + HOST + ":" + PORT + "/hit";
    private final String GET_STATS_PATH = "http://" + HOST + ":" + PORT + "/stats";

    public ResponseEntity<Object> postHit(
            String app, String uri, String ip, LocalDateTime timestamp
    ) throws JsonProcessingException {
        EndpointHit endpointHit = buildEndpointHit(app, uri, ip, timestamp);
        String body = buildBody(endpointHit);
        return makeAndSendRequest(HttpMethod.POST, POST_HIT_PATH, body);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return makeAndSendRequest(
                HttpMethod.GET,
                GET_STATS_PATH + buildParameters(start, end, uris, unique),
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

    private String buildParameters(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    ) {
        StringBuilder builder = new StringBuilder("?start=");
        String startStr = start.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        String endStr = end.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        builder.append(startStr).append("&end=").append(endStr);
        for (String uri : uris) {
            builder.append("&uri=").append(uri);
        }
        if (unique != null) {
            builder.append("&unique=").append(unique);
        }
        return builder.toString();
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(
            HttpMethod method, String path, @Nullable T body
    ) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> shareitServerResponse;
        try {
            shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
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
