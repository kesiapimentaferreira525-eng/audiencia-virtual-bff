package com.audiencia_virtual_bff.BffGlobalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.Instant;

@RestControllerAdvice
public class BffGlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                (HttpStatus) ex.getStatusCode(),
                ex.getReason() != null ? ex.getReason() : "Erro na comunicação com o serviço de domínio");
        problemDetail.setTitle("Erro no Microsserviço de Domínio");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ProblemDetail handleWebClientException(WebClientResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getStatusCode(),
                ex.getResponseBodyAsString());
        problemDetail.setTitle("Falha de Integração (WebClient)");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ProblemDetail handleWebClientRequestException(WebClientRequestException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "O serviço de audiências está indisponível no momento.");
        problemDetail.setTitle("Backend indisponível");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
