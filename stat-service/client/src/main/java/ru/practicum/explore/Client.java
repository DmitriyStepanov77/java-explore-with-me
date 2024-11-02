package ru.practicum.explore;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class Client {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestClient client;

    public Client(@Value("${stat-server.url}") String serverUrl) {
        this.client = RestClient.create(serverUrl);
    }

    public void saveHit(String app, HttpServletRequest request) {
        log.info("Saving hit for app: {}", app);
        HitDto dto = HitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("Create request for stat-service");
        ResponseEntity<Void> response = client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve().toBodilessEntity();
        log.info("Saving hit for app: {} with successful code {}", app, response.getStatusCode());
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end,
                                 List<String> uris, boolean unique) {
        log.info("Getting view stats for uri: {}", uris);
        try {
            return client.get()
                    .uri(uriBuilder -> uriBuilder.path("/stats")
                            .queryParam("start", start.format(FORMATTER))
                            .queryParam("end", end.format(FORMATTER))
                            .queryParam("uris", uris)
                            .queryParam("unique", unique)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is2xxSuccessful,
                            ((request, response) -> log.info("Getting stats for {} with successful code {}", uris,
                                    response.getStatusCode())))
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            log.error("Getting stats for {} with error {}", uris, e.getMessage());
            return Collections.emptyList();
        }
    }
}