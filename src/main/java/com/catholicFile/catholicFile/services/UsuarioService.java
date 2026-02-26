package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
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



    public void atualizar(){

    }

    public void listar() {

    }

    public void excluir() {

    }
}
