package com.audiencia_virtual_bff.BffAudienciaController.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AudienciaRequest(
        @NotNull Long agendaId,
        @NotBlank @Email String email,
        @NotNull LocalDateTime dataAudiencia,
        @NotBlank String siteAgendamento
) {
}
