package com.audiencia_virtual_bff.BffAudienciaController;

import com.audiencia_virtual_bff.BffAudienciaService.BffAudienciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BffAudienciaControllerTest {

    private BffAudienciaService service;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        service = mock(BffAudienciaService.class);
        client = WebTestClient.bindToController(new BffAudienciaController(service)).build();
    }

    @Test
    void deveRetornarListaDeAudiencias() {
        when(service.listarTodas(any())).thenReturn(Mono.just(Map.of("total", 1)));

        client.get()
                .uri("/api/v1/bff/audiencias")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.total").isEqualTo(1);
    }

    @Test
    void deveBuscarAudienciaPorId() {
        when(service.buscarPorId(eq(7L), any())).thenReturn(Mono.just(Map.of("id", 7)));

        client.get()
                .uri("/api/v1/bff/audiencias/7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(7);
    }

    @Test
    void deveCriarAudienciaComStatusCreated() {
        when(service.criarAudiencia(any(), any())).thenReturn(Mono.just(Map.of("id", 8)));

        client.post()
                .uri("/api/v1/bff/audiencias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("nome", "Nova audiencia"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(8);
    }

    @Test
    void deveBuscarPorNome() {
        when(service.buscarPorNome(eq("teste"), any())).thenReturn(Mono.just(Map.of("total", 1)));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bff/audiencias/buscar")
                        .queryParam("nome", "teste")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.total").isEqualTo(1);
    }
}
