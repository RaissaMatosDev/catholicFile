package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TipoSecao;
import com.catholicFile.catholicFile.infra.RecursoNaoEncontradoException;
import com.catholicFile.catholicFile.repositories.FolhetoRepository;
import com.catholicFile.catholicFile.repositories.SecaoRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolhetoService {

    private final TemplateEngine templateEngine;
    private final FolhetoRepository folhetoRepository;
    private final SecaoRepository secaoRepository;

    public FolhetoService(TemplateEngine templateEngine, FolhetoRepository repository,
                          SecaoRepository secaoRepository) {
        this.templateEngine = templateEngine;
        this.folhetoRepository = repository;
        this.secaoRepository = secaoRepository;
    }

    private void validarIds(List<Long> ids) throws RecursoNaoEncontradoException {

        if (ids == null || ids.isEmpty()) {
            throw new RecursoNaoEncontradoException("O folheto precisa ter seções.");
        }

        Set<Long> unicos = new HashSet<>(ids);

        if (unicos.size() != ids.size()) {
            throw new RecursoNaoEncontradoException("Não pode repetir seção.");
        }
    }
    private void validarTipos(List<SecaoFolheto> secoes) throws RecursoNaoEncontradoException {

        Set<TipoSecao> tipos = secoes.stream()
                .map(SecaoFolheto::getTipo)
                .collect(Collectors.toSet());

        if (tipos.size() != secoes.size()) {
            throw new RecursoNaoEncontradoException("Não pode repetir tipo de seção.");
        }
    }

    @Transactional
    public FolhetoDTO cadastrarFolheto(FolhetoDTO dto) throws RecursoNaoEncontradoException {

        validarIds(dto.secoesIds());

        List<SecaoFolheto> secoes = secaoRepository.findAllById(dto.secoesIds());

        if (secoes.size() != dto.secoesIds().size()) {
            throw new RecursoNaoEncontradoException("Uma ou mais seções não existem.");
        }

        validarIds(dto.secoesIds());
        validarTipos(secoes);

        Folheto folheto = new Folheto();
        folheto.setTitulo(dto.titulo());

        secoes.forEach(folheto::adicionarSecao);

        folheto.setSecoes(secoes);

        return new FolhetoDTO(folhetoRepository.save(folheto));
    }

    public Page<FolhetoDTO> listar(Pageable pageable) {
        return folhetoRepository.findAll(pageable).map(FolhetoDTO::new);

    }

    @Transactional
    public void excluir(Long id) throws RecursoNaoEncontradoException {
        if (!folhetoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Folheto não encontrado.");
        }
        folhetoRepository.deleteById(id);
    }

    public byte[] gerarPdf(Long folhetoId) throws Exception {
        // Busca o folheto completo (com seções e conteúdos)
        Folheto folheto = folhetoRepository.findById(folhetoId)
                .orElseThrow(() -> new RuntimeException("Folheto não encontrado"));

        //Faz a ordenação das seções de acordo com o enum TipoSecao ordinalmente
        List<SecaoFolheto> secoesOrdenadas = folheto.getSecoes()
                .stream()
                .sorted(Comparator.comparing(s -> s.getTipo().ordinal()))
                .toList();

        folheto.setSecoes(secoesOrdenadas);
        // Prepara o contexto Thymeleaf
        Context context = new Context();
        context.setVariable("folheto", folheto);

        // Processa o template Thymeleaf
        String htmlFinal = templateEngine.process("folheto", context);

        // Converte para PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(htmlFinal, null);
        builder.toStream(outputStream);
        builder.run();
        builder.useFastMode();

        return outputStream.toByteArray();

    }
}
