package com.catholicfile.catholicfile.services;

import com.catholicfile.catholicfile.dtos.FolhetoDTO;
import com.catholicfile.catholicfile.entities.Folheto;
import com.catholicfile.catholicfile.entities.SecaoFolheto;
import com.catholicfile.catholicfile.enums.TipoSecao;
import com.catholicfile.catholicfile.infra.RecursoNaoEncontradoException;
import com.catholicfile.catholicfile.repositories.FolhetoRepository;
import com.catholicfile.catholicfile.repositories.SecaoRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.transaction.annotation.Transactional; // Correto
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
            throw new RecursoNaoEncontradoException(HttpStatus.NOT_FOUND, "O folheto precisa ter seções.");
        }

        Set<Long> unicos = new HashSet<>(ids);

        if (unicos.size() != ids.size()) {
            throw new RecursoNaoEncontradoException(HttpStatus.NOT_FOUND, "Não pode repetir seção.");
        }
    }
    private void validarTipos(List<SecaoFolheto> secoes) throws RecursoNaoEncontradoException {

        Set<TipoSecao> tipos = secoes.stream()
                .map(SecaoFolheto::getTipo)
                .collect(Collectors.toSet());

        if (tipos.size() != secoes.size()) {
            throw new RecursoNaoEncontradoException(HttpStatus.NOT_FOUND, "Não pode repetir tipo de seção.");
        }
    }

    @Transactional
    public FolhetoDTO cadastrarFolheto(FolhetoDTO dto) {

        validarIds(dto.secoesIds());

        List<SecaoFolheto> secoes = secaoRepository.findAllById(dto.secoesIds());

        if (secoes.size() != dto.secoesIds().size()) {
            throw new RecursoNaoEncontradoException(HttpStatus.NOT_FOUND, "Uma ou mais seções não existem.");
        }

        validarTipos(secoes);

        Folheto folheto = new Folheto();
        folheto.setTitulo(dto.titulo());
        folheto.setLit(dto.lit());

        secoes.forEach(folheto::adicionarSecao);

        Folheto salvo = folhetoRepository.save(folheto);

        return new FolhetoDTO(salvo);
    }

    @Transactional
    public void excluir(Long id) throws RecursoNaoEncontradoException {
        if (!folhetoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(HttpStatus.NOT_FOUND, "Folheto não encontrado.");
        }
        folhetoRepository.deleteById(id);
    }
    @Transactional(readOnly = true) // Use readOnly para melhorar a performance em geração de arquivos
    public byte[] gerarPdf(Long folhetoId) throws Exception {

        Folheto folheto = folhetoRepository.findById(folhetoId)
                .orElseThrow(() -> new RuntimeException("Folheto não encontrado"));

        List<SecaoFolheto> secoesOrdenadas = folheto.getSecoes()
                .stream()
                .sorted(Comparator.comparing(s -> s.getTipo().ordinal()))
                .toList();


        Context context = new Context();
        context.setVariable("folheto", folheto);


        context.setVariable("secoes", secoesOrdenadas);

        // Processa o template Thymeleaf
        String htmlFinal = templateEngine.process("folheto", context);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(htmlFinal, "");
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
    public Page<FolhetoDTO> listar(Pageable pageable) {
        return folhetoRepository.findAll(pageable)
                .map(folheto -> new FolhetoDTO(
                        folheto.getId(),
                        folheto.getTitulo(),
                        folheto.getLit(),
                        folheto.getSecoes().stream()
                                .map(SecaoFolheto::getId)
                                .toList()
                ));
    }
}
