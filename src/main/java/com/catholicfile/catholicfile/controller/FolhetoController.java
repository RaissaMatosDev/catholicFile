package com.catholicfile.catholicfile.controller;


import com.catholicfile.catholicfile.configurations.SecurityConfigurations;
import com.catholicfile.catholicfile.dtos.ErroDTO;
import com.catholicfile.catholicfile.dtos.FolhetoDTO;
import com.catholicfile.catholicfile.dtos.PageResponseDTO;
import com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO;
import com.catholicfile.catholicfile.infra.RecursoNaoEncontradoException;
import com.catholicfile.catholicfile.services.FolhetoService;
import com.catholicfile.catholicfile.services.SecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/folheto")
@Tag(name = "Folheto", description = "Endpoints para gerenciamento de folhetos")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class FolhetoController {

    private final FolhetoService folhetoService;
    private final SecaoService secaoService;


    public FolhetoController(FolhetoService folhetoService, SecaoService secaoService) {
        this.folhetoService = folhetoService;
        this.secaoService = secaoService;
    }


    @Operation(summary = "Gera PDF de um folheto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso", content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Folheto não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) throws Exception {
        byte[] pdf = folhetoService.gerarPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=folheto.pdf")
                .body(pdf);
    }
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Lista todas as seções de um folheto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seções listadas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Folheto não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<SecaoFolhetoDTO>> listarSecoes(@PathVariable Long id) {
        List<SecaoFolhetoDTO> secoes = folhetoService.buscarSecoesPorFolheto(id);
        if (secoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(secoes);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')") // Padronizado para Authority
    @Operation(summary = "Lista todos os folhetos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping
    public Page<FolhetoDTO> listar(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        return folhetoService.listar(pageable);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')") // Padronizado para Authority
    @Operation(summary = "Cadastra um novo folheto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Folheto criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolhetoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Uma ou mais seções não existem", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<FolhetoDTO> cadastrarFolheto(@RequestBody @Valid FolhetoDTO dados)
            throws RecursoNaoEncontradoException {
        var folhetoSalvo = folhetoService.cadastrarFolheto(dados);
        return ResponseEntity
                .created(URI.create("/folheto/" + folhetoSalvo.id()))
                .body(folhetoSalvo);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')") // Padronizado para Authority
    @Operation(summary = "Exclui um folheto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Folheto excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Folheto não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFolheto(@PathVariable Long id) throws RecursoNaoEncontradoException {
        folhetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
