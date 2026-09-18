package com.audiencia_virtual_bff.BffAudienciaController.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AudienciaRequest(
                @NotBlank String agendaNome,
                @NotBlank String parteNome,
                @NotBlank String parteCpf,
                @NotBlank String parteNumeroProcesso,
                @NotBlank @Email String email,
                @NotNull LocalDateTime dataAudiencia,
                @NotBlank String siteAgendamento) {
}
