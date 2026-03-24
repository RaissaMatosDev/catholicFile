package com.catholicfile.catholicfile.dtos;

import com.catholicfile.catholicfile.entities.Usuario;
import com.catholicfile.catholicfile.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retorno das informações pós cadastro do e listagem usuário")
public record UsuarioDTO(
        @Schema(description = "O id permanece o mesmo")
        Long id,

        @Schema(description = "Nome do Usuario", example = "Beatriz Matos")
        String nome,

        @Schema(description = "Email do usuario", example = "beatriz@email.com")
        String email,

        @Schema(description = "Tipo de Autorização", example = "ADMINISTRADOR")
        UserRole role
) {

    public UsuarioDTO(Usuario usuario) {
            this(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole()
            );
    }
}

