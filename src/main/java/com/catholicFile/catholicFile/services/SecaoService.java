package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.SecaoFolhetoDTO;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.repositories.SecaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecaoService {

        private final SecaoRepository repository;

        public SecaoService(SecaoRepository repository) {
            this.repository = repository;
        }

        public List<SecaoFolheto> buscarSecoesPorFolheto(Long folhetoId) {
        return repository.findByFolhetoId(folhetoId);
        }

        @Transactional
        public SecaoFolheto criar(SecaoFolhetoDTO dto) {

        SecaoFolheto secao = new SecaoFolheto();
        secao.setTipo(dto.tipo());
        secao.setConteudo(dto.conteudo());

        return repository.save(secao);
        }

        public List<SecaoFolheto> listar() {
            return repository.findAll();
        }

        @Transactional
        public void excluir(Long id) {
            repository.deleteById(id);
        }

    public SecaoFolheto atualizar(Long id, SecaoFolhetoDTO dto) {

        SecaoFolheto secao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seção não encontrada"));

        secao.setTipo(dto.tipo());
        secao.setConteudo(dto.conteudo());

        return repository.save(secao);
    }

    
}


