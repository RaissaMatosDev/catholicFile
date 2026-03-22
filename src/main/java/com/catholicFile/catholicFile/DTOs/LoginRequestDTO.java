package com.catholicFile.catholicFile.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
        @Schema(description = "Email", example = "Raissa@hotmail.com")
        String email,

        @Schema(description = "Senha", example = "gatoFeio123")
        String senha
) {}
