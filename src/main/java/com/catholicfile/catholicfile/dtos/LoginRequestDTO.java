package com.catholicfile.catholicfile.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
        @Schema(description = "Email", example = "administrador@teste.com")
        String email,

        @Schema(description = "Senha", example = "12345678")
        String senha
) {}
