package com.catholicFile.catholicFile.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação")
public record LoginResponseDTO(

        @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tipo

) {}
