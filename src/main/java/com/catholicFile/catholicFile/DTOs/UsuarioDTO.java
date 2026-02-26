package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(

                         Long id,

                         @NotBlank
                         String nome,

                         @NotNull @Email
                         String email,

                         @NotBlank @Valid
                         String senha,

                         UserRole role

                         ) {
    public UsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getRole()
        );
    }
}
