package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.Configurations.JwtUtil;
import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioCadastroDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.enums.UserRole;
import com.catholicFile.catholicFile.infra.RecursoNaoEncontradoException;
import com.catholicFile.catholicFile.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

private final UsuarioRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(UsuarioRepository repository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public UsuarioDTO cadastrar(@Valid UsuarioCadastroDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(UserRole.USUARIO); // garante segurança

        repository.save(usuario);

        return new UsuarioDTO(usuario);
    }
    @Transactional
    public UsuarioDTO atualizarMeuPerfil(UsuarioAttDTO dto, String emailLogado) {
        System.out.println("EMAIL DO TOKEN: " + emailLogado);
        var usuario = repository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        // valida senha atual
        if (dto.senhaAtual() != null) {
            if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta");
            }
        }

        // atualiza dados
        if (dto.nome() != null) usuario.setNome(dto.nome());
        if (dto.email() != null) usuario.setEmail(dto.email());

        if (dto.novaSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        return new UsuarioDTO(usuario);
    }
    @Transactional
    public UsuarioDTO atualizarPorId(Long id, UsuarioAttDTO dto) {

        var usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.atualizarInformacoes(dto);

        return new UsuarioDTO(usuario);
    }

    public Page<UsuarioCadastroDTO> listar(Pageable pageable) {
        return repository.findAll(pageable).map(UsuarioCadastroDTO::new);

    }
    @Transactional
    public void excluir(Long id) {
        var usuario = repository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException(
                HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        repository.deleteById(id);
    }

    public String autenticar(String email, String senha) {

        var usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        return jwtUtil.gerarToken(usuario.getEmail());
    }


}
