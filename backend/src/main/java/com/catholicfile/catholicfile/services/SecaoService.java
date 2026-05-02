package com.catholicfile.catholicfile.services;

import com.catholicfile.catholicfile.dtos.PageResponseDTO;
import com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO;
import com.catholicfile.catholicfile.entities.SecaoFolheto;
import com.catholicfile.catholicfile.enums.TempoLit;
import com.catholicfile.catholicfile.enums.TipoSecao;
import com.catholicfile.catholicfile.infra.RecursoNaoEncontradoException;
import com.catholicfile.catholicfile.repositories.SecaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SecaoService {

    private final SecaoRepository repository;

    public SecaoService(SecaoRepository repository) {
        this.repository = repository;
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

        String palavraBusca = (palavra != null && !palavra.trim().isEmpty()) ? palavra : null;


        Page<SecaoFolheto> page = repository.filtrar(palavraBusca, tipo, lit, pageable);

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

        if (dto.titulo() != null) {
            secao.setTitulo(dto.titulo());
        }

        if (dto.conteudo() != null) {
            secao.setConteudo(dto.conteudo());
        }

        if (dto.tipo() != null) {
            secao.setTipo(dto.tipo());
        }

        if (dto.lit() != null) {
            secao.setLit(dto.lit());
        }

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