package com.catholicfile.catholicfile.entities;

import com.catholicfile.catholicfile.dtos.UsuarioAttDTO;
import com.catholicfile.catholicfile.dtos.UsuarioCadastroDTO;
import com.catholicfile.catholicfile.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "usuarios")

public class Usuario implements UserDetails {

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


    public Usuario(UsuarioCadastroDTO dto) {
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
    public Usuario(String nome, String email, String senha, UserRole role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // retorna a senha armazenada
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // usuário sempre habilitado
    }

}
