package com.audiencia_virtual_bff.BffAudienciaController;

import com.audiencia_virtual_bff.BffAudienciaService.BffAudienciaService;
import com.audiencia_virtual_bff.BffAudienciaController.dto.AudienciaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/bff/audiencias")
@Tag(name = "Audiências virtuais", description = "Operações públicas do gateway de audiências virtuais")
public class BffAudienciaController {

    private final BffAudienciaService service;

    public BffAudienciaController(BffAudienciaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar audiências virtuais")
    @ApiResponse(responseCode = "200", description = "Lista retornada pelo backend")
    public Mono<ResponseEntity<Object>> listar(@RequestHeader HttpHeaders headers) {
        return service.listarTodas(headers)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar audiência por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Audiência encontrada"),
            @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    })
    public Mono<ResponseEntity<Object>> buscarPorId(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return service.buscarPorId(id, headers)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Criar audiência virtual")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Audiência criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Agenda já utilizada"),
            @ApiResponse(responseCode = "503", description = "Backend indisponível")
    })
    public Mono<ResponseEntity<Object>> criar(
            @Valid @RequestBody AudienciaRequest payload,
            @RequestHeader HttpHeaders headers) {
        return service.criarAudiencia(payload, headers)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar audiências por nome da parte")
    public Mono<ResponseEntity<Object>> buscarPorNome(@RequestParam String nome, @RequestHeader HttpHeaders headers) {
        return service.buscarPorNome(nome, headers)
                .map(ResponseEntity::ok);
    }
}