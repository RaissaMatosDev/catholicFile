package com.catholicFile.catholicFile.services;

import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TipoSecao;
import com.catholicFile.catholicFile.infra.RegraNegocioException;
import com.catholicFile.catholicFile.repositories.FolhetoRepository;
import com.catholicFile.catholicFile.repositories.SecaoRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolhetoService {

    private final SecaoService secaoService;
    private final FolhetoRepository folhetoRepository;
    private final SecaoRepository secaoRepository;
    private static final int MIN_SECOES = 5;
    private static final int MAX_SECOES = 10;

    public FolhetoService(SecaoService secaoService, FolhetoRepository repository,
                          SecaoRepository secaoRepository) {
        this.secaoService = secaoService;
        this.folhetoRepository = repository;
        this.secaoRepository = secaoRepository;
    }

    private void validarIds(List<Long> ids) throws RegraNegocioException {

        if (ids == null || ids.isEmpty()) {
            throw new RegraNegocioException("O folheto precisa ter seções.");
        }

        Set<Long> unicos = new HashSet<>(ids);

        if (unicos.size() != ids.size()) {
            throw new RegraNegocioException("Não pode repetir seção.");
        }
    }
    private void validarTipos(List<SecaoFolheto> secoes) throws RegraNegocioException {

        Set<TipoSecao> tipos = secoes.stream()
                .map(SecaoFolheto::getTipo)
                .collect(Collectors.toSet());

        if (tipos.size() != secoes.size()) {
            throw new RegraNegocioException("Não pode repetir tipo de seção.");
        }
    }

    @Transactional
    public FolhetoDTO cadastrarFolheto(FolhetoDTO dto) throws RegraNegocioException {

        validarIds(dto.secoesIds());

        List<SecaoFolheto> secoes = secaoRepository.findAllById(dto.secoesIds());

        if (secoes.size() != dto.secoesIds().size()) {
            throw new RegraNegocioException("Uma ou mais seções não existem.");
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
    public void excluir(Long id) throws RegraNegocioException {
        if (!folhetoRepository.existsById(id)) {
            throw new RegraNegocioException("Folheto não encontrado.");
        }
        folhetoRepository.deleteById(id);
    }

    public byte[] gerarPdf(Long folhetoId) throws Exception {

        List<SecaoFolheto> secoes =
                secaoService.buscarSecoesPorFolheto(folhetoId);

        StringBuilder secoesHtml = new StringBuilder();

        for (SecaoFolheto secao : secoes) {

            secoesHtml.append("""
                <div class="secao">
                    <div class="tipo">%s</div>
                    <div class="conteudo">%s</div>
                </div>
            """.formatted(
                    secao.getTipo(),
                    secao.getConteudo()
            ));
        }

        String template = Files.readString(
                Paths.get("src/main/resources/templates/folheto.html")
        );

        String htmlFinal = template.replace("{{secoes}}", secoesHtml.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(htmlFinal, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
}
