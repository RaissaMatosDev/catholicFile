package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioDTO cadastrar(UsuarioDTO dto) {
        Usuario usuario = new Usuario(dto);
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
}
