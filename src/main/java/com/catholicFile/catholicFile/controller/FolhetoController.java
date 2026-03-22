package com.catholicFile.catholicFile.controller;


import com.catholicFile.catholicFile.Configurations.SecurityConfigurations;
import com.catholicFile.catholicFile.DTOs.ErroDTO;
import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.DTOs.PageResponseDTO;
import com.catholicFile.catholicFile.infra.RecursoNaoEncontradoException;
import com.catholicFile.catholicFile.services.FolhetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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


@RestController
@RequestMapping("/folheto")
@Tag(name = "Folheto", description = "Endpoints para gerenciamento de folhetos")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class FolhetoController {

    private final FolhetoService folhetoService;

    public FolhetoController(FolhetoService folhetoService) {
        this.folhetoService = folhetoService;
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Gera PDF de um folheto pelo ID")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "PDF gerado com sucesso",
                    content = @Content(
                            mediaType = "application/pdf"
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Folheto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno no servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) throws Exception {
        byte[] pdf = folhetoService.gerarPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=folheto.pdf")
                .body(pdf);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Lista todos os folhetos paginados")

    @Parameters({
            @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
            @Parameter(name = "size", description = "Quantidade de itens por página", example = "10"),
            @Parameter(name = "sort", description = "Ordenação (ex: titulo,asc)", example = "titulo,asc")
    })

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponseDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @GetMapping
    public Page<FolhetoDTO> listar(Pageable pageable) {
        return folhetoService.listar(pageable);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Cadastra um novo folheto")

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Folheto criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FolhetoDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Uma ou mais seções não existem",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<FolhetoDTO> cadastrarFolheto(@RequestBody @Valid FolhetoDTO dados)
            throws RecursoNaoEncontradoException {
        var folhetoSalvo = folhetoService.cadastrarFolheto(dados);
        return ResponseEntity
                .created(URI.create("/folheto/" + folhetoSalvo.id()))
                .body(folhetoSalvo);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Exclui um folheto pelo ID")

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "204",
                    description = "Folheto excluído com sucesso",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Folheto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFolheto(@PathVariable Long id) throws RecursoNaoEncontradoException {
        folhetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}