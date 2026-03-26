package com.catholicfile.catholicfile.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErroDTO (
        @Schema(example = "2026-03-26T19:33:00")
        LocalDateTime timestamp,

        @Schema(example = "404")
        int status,

        @Schema(example = "Não encontrado")
        String error,

        @Schema(example = "Não encontrado")
        String mensagem,

        @Schema(example = "/folheto/1")
        String path

) {}

