package com.audiencia_virtual_bff.BffAudienciaController;

import com.audiencia_virtual_bff.BffAudienciaService.BffAudienciaService;
import com.audiencia_virtual_bff.BffAudienciaController.dto.AudienciaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bff/audiencias")
public class BffAudienciaController {

    private final BffAudienciaService service;

    public BffAudienciaController(BffAudienciaService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> listar(@RequestHeader HttpHeaders headers) {
        return service.listarTodas(headers)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> buscarPorId(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return service.buscarPorId(id, headers)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> criar(
            @Valid @RequestBody AudienciaRequest payload,
            @RequestHeader HttpHeaders headers) {
        return service.criarAudiencia(payload, headers)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/buscar")
    public Mono<ResponseEntity<Object>> buscarPorNome(@RequestParam String nome, @RequestHeader HttpHeaders headers) {
        return service.buscarPorNome(nome, headers)
                .map(ResponseEntity::ok);
    }
}