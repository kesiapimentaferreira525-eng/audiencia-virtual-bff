package com.audiencia_virtual_bff.BffAudienciaService;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BffAudienciaServiceTest {

    @Test
    void deveListarAudienciasEEncaminharHeaders() {
        ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("[{\"id\":1,\"nome\":\"Audiencia teste\"}]")
                .build();

        when(exchangeFunction.exchange(any())).thenAnswer(invocation -> {
            ClientRequest request = invocation.getArgument(0);
            assertThat(request.headers().getFirst(HttpHeaders.AUTHORIZATION))
                    .isEqualTo("Bearer token");
            assertThat(request.headers().getFirst("X-Correlation-Id"))
                    .isEqualTo("correlation-id");
            assertThat(request.url().getPath())
                    .isEqualTo("/v1/audiencias-virtuais");
            return Mono.just(response);
        });

        BffAudienciaService service = new BffAudienciaService(
                WebClient.builder().exchangeFunction(exchangeFunction).build());
        HttpHeaders headers = headers();

        StepVerifier.create(service.listarTodas(headers))
                .assertNext(result -> assertThat(result).isInstanceOfAny(java.util.List.class))
                .verifyComplete();
    }

    @Test
    void deveBuscarAudienciaPorId() {
        ExchangeFunction exchangeFunction = mockExchange(HttpStatus.OK, "{\"id\":7}");
        BffAudienciaService service = service(exchangeFunction);

        StepVerifier.create(service.buscarPorId(7L, new HttpHeaders()))
                .assertNext(result -> assertThat(result).isInstanceOf(Map.class))
                .verifyComplete();
    }

    @Test
    void deveCriarAudiencia() {
        ExchangeFunction exchangeFunction = mockExchange(HttpStatus.CREATED, "{\"id\":8}");
        BffAudienciaService service = service(exchangeFunction);

        StepVerifier.create(service.criarAudiencia(Map.of("nome", "Nova audiencia"), new HttpHeaders()))
                .assertNext(result -> assertThat(result).isInstanceOf(Map.class))
                .verifyComplete();
    }

    @Test
    void deveBuscarAudienciasPorNome() {
        ExchangeFunction exchangeFunction = mockExchange(HttpStatus.OK, "[{\"id\":1}]");
        BffAudienciaService service = service(exchangeFunction);

        StepVerifier.create(service.buscarPorNome("Audiencia teste", new HttpHeaders()))
                .assertNext(result -> assertThat(result).isInstanceOfAny(java.util.List.class))
                .verifyComplete();
    }

    @Test
    void devePropagarErroDaApi() {
        ExchangeFunction exchangeFunction = mockExchange(HttpStatus.NOT_FOUND, "Audiencia nao encontrada");
        BffAudienciaService service = service(exchangeFunction);

        StepVerifier.create(service.buscarPorId(99L, new HttpHeaders()))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ResponseStatusException.class);
                    assertThat(((ResponseStatusException) error).getStatusCode())
                            .isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(error.getMessage()).contains("Audiencia nao encontrada");
                })
                .verify();
    }

    private BffAudienciaService service(ExchangeFunction exchangeFunction) {
        return new BffAudienciaService(WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build());
    }

    private ExchangeFunction mockExchange(HttpStatus status, String body) {
        ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(
                ClientResponse.create(status)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(body)
                        .build()));
        return exchangeFunction;
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("token");
        headers.set("X-Correlation-Id", "correlation-id");
        return headers;
    }
}
