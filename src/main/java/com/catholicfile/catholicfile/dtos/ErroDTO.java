package com.catholicfile.catholicfile.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErroDTO (

        @Schema(example = "404")
            int status,

            @Schema(example = "Não encontrado")
            String mensagem,

            @Schema(example = "2026-03-22T17:45:30")
            LocalDateTime timestamp
    ) {}

