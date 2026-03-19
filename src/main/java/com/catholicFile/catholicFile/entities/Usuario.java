package com.catholicFile.catholicFile.entities;

import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "usuarios")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Usuario(UsuarioDTO dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.senha = dto.senha();
        this.role = dto.role();
    }
    public void atualizarInformacoes(UsuarioAttDTO dto) {
        if (dto.nome() != null) {
            this.nome = dto.nome();
        }
        if (dto.email() != null) {
            this.email = dto.email();
        }
    }

}
