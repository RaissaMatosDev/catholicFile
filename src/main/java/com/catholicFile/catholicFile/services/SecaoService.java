package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.PageResponseDTO;
import com.catholicFile.catholicFile.DTOs.SecaoFolhetoDTO;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;
import com.catholicFile.catholicFile.infra.RecursoNaoEncontradoException;
import com.catholicFile.catholicFile.repositories.SecaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SecaoService {

    private final SecaoRepository repository;

    public SecaoService(SecaoRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca todas as seções de um folheto, ordenadas pelo TipoSecao
     */
    public List<SecaoFolhetoDTO> buscarSecoesPorFolheto(Long folhetoId) {
        List<SecaoFolheto> secoes = repository.findByFolhetoId(folhetoId);
        secoes.sort(Comparator.comparing(s -> s.getTipo().ordinal()));

        return secoes.stream()
                .map(SecaoFolhetoDTO::new)
                .toList();
    }

    /**
     * Filtra seções por palavra, tipo e tempo litúrgico
     */
    public Page<SecaoFolhetoDTO> filtrar(
            String palavra,
            TipoSecao tipo,
            TempoLit lit,
            Pageable pageable
    ) {
        Page<SecaoFolheto> page = repository.filtrar(palavra, tipo, lit, pageable);

        if (page.isEmpty()) {
            throw new RecursoNaoEncontradoException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma seção encontrada com os filtros aplicados."
            );
        }

        return page.map(SecaoFolhetoDTO::new);
    }

    // Cria nova seção
    @Transactional
    public SecaoFolheto criar(SecaoFolhetoDTO dto) {
        SecaoFolheto secao = new SecaoFolheto();
        secao.setTipo(dto.tipo());
        secao.setConteudo(dto.conteudo());
        secao.setLit(dto.lit());
        secao.setTitulo(dto.titulo());
        return repository.save(secao);
    }

    //Lista todas as seções como DTOs
    public PageResponseDTO<SecaoFolhetoDTO> listarDTO(Pageable pageable) {
        Page<SecaoFolheto> page = repository.findAll(pageable);

        Page<SecaoFolhetoDTO> dtoPage = page.map(SecaoFolhetoDTO::new);

        return PageResponseDTO.from(dtoPage);
    }


    // Atualiza uma seção existente
    @Transactional
    public SecaoFolheto atualizar(Long id, SecaoFolhetoDTO dto) {
        SecaoFolheto secao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        HttpStatus.NOT_FOUND, "Seção não encontrada"));

        secao.setTipo(dto.tipo());
        secao.setConteudo(dto.conteudo());
        secao.setLit(dto.lit());
        secao.setTitulo(dto.titulo());

        return repository.save(secao);
    }

    //Exclui uma seção
    @Transactional
    public void excluir(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        HttpStatus.NOT_FOUND, "Seção não encontrada"));
        repository.deleteById(id);
    }
}