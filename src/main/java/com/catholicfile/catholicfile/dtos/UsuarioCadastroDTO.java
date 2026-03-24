package com.catholicfile.catholicfile.dtos;

import com.catholicfile.catholicfile.entities.Usuario;
import com.catholicfile.catholicfile.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(description = "Informações de cadastro de usuário")
public record UsuarioCadastroDTO(

                        @Schema(description = "O id é dinamico não podendo ser modificado")
                         Long id,

                        @Schema(description = "O nome completo do usuário")
                         @NotBlank
                         String nome,

                        @Schema(description = "Email do usuário")
                         @NotNull @Email
                         String email,

                        @Schema(description = "Senha do usuario(será armazenada em hash")
                         @NotBlank
                         String senha,

                        @Schema(description = "Apenas um ADMINISTRADOR pode criar um usuário ADMINISTRADOR,em caso de tentativa sem autenticação o programa troca para usuario comum mesmo digitando ADIMINISTRADOR")
                         UserRole role

                         ) {

    public UsuarioCadastroDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getRole()
        );
    }
}
