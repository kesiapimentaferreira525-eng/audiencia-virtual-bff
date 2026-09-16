package com.audiencia_virtual_bff.BffAudienciaService;

import com.audiencia_virtual_bff.BffAudienciaController.dto.AudienciaRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class BffAudienciaService {

    private final WebClient audienciaWebClient;

    public BffAudienciaService(WebClient audienciaWebClient) {
        this.audienciaWebClient = audienciaWebClient;
    }

    public Mono<Object> listarTodas(HttpHeaders incomingHeaders) {
        return audienciaWebClient.get()
                .uri("/v1/audiencias-virtuais")
                .headers(headers -> copyRelevantHeaders(incomingHeaders, headers))
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResponseStatusException(response.statusCode(), errorBody)))
                )
                .bodyToMono(Object.class);
    }

    public Mono<Object> buscarPorId(Long id, HttpHeaders incomingHeaders) {
        return audienciaWebClient.get()
                .uri("/v1/audiencias-virtuais/{id}", id)
                .headers(headers -> copyRelevantHeaders(incomingHeaders, headers))
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResponseStatusException(response.statusCode(), errorBody)))
                )
                .bodyToMono(Object.class);
    }

    public Mono<Object> criarAudiencia(AudienciaRequest payload, HttpHeaders incomingHeaders) {
        return audienciaWebClient.post()
                .uri("/v1/audiencias-virtuais")
                .headers(headers -> copyRelevantHeaders(incomingHeaders, headers))
                .bodyValue(payload)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResponseStatusException(response.statusCode(), errorBody)))
                )
                .bodyToMono(Object.class);
    }

    public Mono<Object> buscarPorNome(String nome, HttpHeaders incomingHeaders) {
        return audienciaWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/audiencias-virtuais/buscar")
                        .queryParam("nome", nome)
                        .build())
                .headers(headers -> copyRelevantHeaders(incomingHeaders, headers))
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResponseStatusException(response.statusCode(), errorBody)))
                )
                .bodyToMono(Object.class);
    }

    private void copyRelevantHeaders(HttpHeaders sourceHeaders, HttpHeaders targetHeaders) {
        String authorization = sourceHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {
            targetHeaders.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        String correlationId = sourceHeaders.getFirst("X-Correlation-Id");
        if (correlationId != null) {
            targetHeaders.set("X-Correlation-Id", correlationId);
        }
    }
}
