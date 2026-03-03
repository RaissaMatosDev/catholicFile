package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAttDTO(

                            @NotBlank
                            String nome,

                            Long id,

                            @NotNull(message = "Email inválido!")
                            @Email(message = "Email é inválido!")
                            String email,

                            @NotBlank @Valid
                            String senha,

                            UserRole USUARIO)

{}
