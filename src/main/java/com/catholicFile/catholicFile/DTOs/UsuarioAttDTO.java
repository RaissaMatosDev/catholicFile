package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAttDTO(

                            @NotNull @Email
                            String email,

                            @NotBlank @Valid
                            String senha,

                            UserRole USUARIO)
{

    public void atualizarUsuario(Long usuarios) {
    }
}
