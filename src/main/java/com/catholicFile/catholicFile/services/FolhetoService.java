package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.repositories.FolhetoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FolhetoService {

    private final FolhetoRepository repository;

    public FolhetoService(FolhetoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FolhetoDTO cadastrarFolheto(FolhetoDTO dto) {
        Folheto folheto = new Folheto(dto);
        Folheto folhetoSalvo = repository.save(folheto);
        return new FolhetoDTO(folhetoSalvo);
    }

    public Page<FolhetoDTO> listar(Pageable pageable) {
        return repository.findAll(pageable).map(FolhetoDTO::new);

    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
