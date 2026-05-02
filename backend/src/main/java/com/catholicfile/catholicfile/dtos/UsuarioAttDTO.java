package com.catholicfile.catholicfile.dtos;

import com.catholicfile.catholicfile.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Informações que um usuario pode alterar")
public record UsuarioAttDTO(

                            @Schema(description = "Nome", example = "Camila Silva")
                            String nome,

                            @Schema(description = "Email", example = "Raissa@hotmail.com")
                            @NotNull(message = "Email inválido!")
                            @Email(message = "Email é inválido!")
                            String email,

                            @Schema(description = "Senha", example = "gatoFeio123")
                            String senhaAtual,

                            @Schema(description = "Senha atualização", example = "gatoFeio1234")
                            String novaSenha,

                            @Schema(description = "Permissão do usuario tambem pode ser alterada", example = "USUARIO, ADMINISTRADOR")
                            UserRole role)

{}

