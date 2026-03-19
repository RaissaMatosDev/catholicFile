package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.Configurations.JwtUtil;
import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

private final UsuarioRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public UsuarioDTO cadastrar(UsuarioDTO dto) {
        Usuario usuario = new Usuario(dto);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.save(usuario);
        return new UsuarioDTO(usuario);
    }

    @Transactional
    public UsuarioDTO atualizar(UsuarioAttDTO dto){
        var usuario = repository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.atualizarInformacoes(dto);
        return new UsuarioDTO(usuario);

    }

    public Page<UsuarioDTO> listar(Pageable pageable) {
        return repository.findAll(pageable).map(UsuarioDTO::new);

    }
    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public String autenticar(String email, String senha) {
        var usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        System.out.println("Senha confere, gerando token...");
        // Gera o token JWT usando o email do usuário
        return jwtUtil.gerarToken(usuario.getEmail());
    }
}
