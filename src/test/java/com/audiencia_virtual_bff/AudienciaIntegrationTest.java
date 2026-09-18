package com.audiencia_virtual_bff;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AudienciaIntegrationTest {

        private static MockWebServer backend;

        @LocalServerPort
        private int port;

        private WebTestClient client;

        @BeforeAll
        static void startBackend() throws IOException {
                backend = new MockWebServer();
                backend.start();
        }

        @BeforeEach
        void createClient() {
                client = WebTestClient.bindToServer()
                                .baseUrl("http://localhost:" + port)
                                .build();
        }

        @AfterAll
        static void stopBackend() throws IOException {
                backend.shutdown();
        }

        @DynamicPropertySource
        static void configureBackend(DynamicPropertyRegistry registry) {
                registry.add("servico.audiencias.url",
                                () -> backend.url("/").toString().replaceAll("/$", ""));
        }

        @Test
        void deveIntegrarControllerServiceEApiAoListarAudiencias() {
                backend.enqueue(new MockResponse()
                                .setResponseCode(200)
                                .addHeader("Content-Type", "application/json")
                                .setBody("[{\"id\":1,\"nome\":\"Audiencia integrada\"}]"));

                client.get()
                                .uri("/api/v1/bff/audiencias")
                                .header("Authorization", "Bearer token")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$[0].id").isEqualTo(1)
                                .jsonPath("$[0].nome").isEqualTo("Audiencia integrada");
        }

        @Test
        void deveIntegrarControllerServiceEApiAoCriarAudiencia() {
                backend.enqueue(new MockResponse()
                                .setResponseCode(201)
                                .addHeader("Content-Type", "application/json")
                                .setBody("{\"id\":2,\"nome\":\"Audiencia criada\"}"));

                client.post()
                                .uri("/api/v1/bff/audiencias")
                                .bodyValue("""
                                                {
                                                  "agendaNome": "Agenda - Carlos Oliveira",
                                                  "parteNome": "Carlos Oliveira",
                                                  "parteCpf": "12345678901",
                                                  "parteNumeroProcesso": "0000001-00.2026.8.05.0001",
                                                  "email": "maria.silva@exemplo.com",
                                                  "dataAudiencia": "2026-10-10T14:00:00",
                                                  "siteAgendamento": "Microsoft Teams"
                                                }
                                                """)
                                .header("Content-Type", "application/json")
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody()
                                .jsonPath("$.id").isEqualTo(2)
                                .jsonPath("$.nome").isEqualTo("Audiencia criada");
        }

        @Test
        void deveRejeitarCriacaoComCamposObrigatoriosInvalidos() {
                client.post()
                                .uri("/api/v1/bff/audiencias")
                                .bodyValue("""
                                                {
                                                  "agendaNome": "Agenda - Carlos Oliveira",
                                                  "parteNome": "Carlos Oliveira",
                                                  "parteCpf": "12345678901",
                                                  "parteNumeroProcesso": "0000001-00.2026.8.05.0001",
                                                  "email": "email-invalido",
                                                  "dataAudiencia": null,
                                                  "siteAgendamento": ""
                                                }
                                                """)
                                .header("Content-Type", "application/json")
                                .exchange()
                                .expectStatus().isBadRequest();
        }
}
