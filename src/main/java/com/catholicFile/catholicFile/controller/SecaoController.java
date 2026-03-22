package com.catholicFile.catholicFile.controller;

import com.catholicFile.catholicFile.Configurations.SecurityConfigurations;
import com.catholicFile.catholicFile.DTOs.ErroDTO;
import com.catholicFile.catholicFile.DTOs.PageResponseDTO;
import com.catholicFile.catholicFile.DTOs.SecaoFolhetoDTO;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;
import com.catholicFile.catholicFile.services.SecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secoes")
@Tag(name = "Seção", description = "Endpoints para gerenciamento de seções")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class SecaoController {

    private final SecaoService secaoService;

    public SecaoController(SecaoService secaoService) {
        this.secaoService = secaoService;
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Lista todas as seções de um folheto pelo ID")
    @ApiResponse(responseCode = "200", description = "Seções listadas com sucesso")
    @ApiResponse(responseCode = "404",
            description = "Folheto não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<List<SecaoFolhetoDTO>> listarSecoes(@PathVariable Long id) {
        List<SecaoFolhetoDTO> secoes = secaoService.buscarSecoesPorFolheto(id);
        return ResponseEntity.ok(secoes);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Filtra seções por palavra, tipo ou tempo litúrgico")

    @Parameters({
            @Parameter(name = "palavra", description = "Palavra-chave para busca", example = "glória"),
            @Parameter(name = "tipo", description = "Tipo da seção", example = "ENTRADA"),
            @Parameter(name = "lit", description = "Tempo litúrgico", example = "QUARESMA")
    })

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Seções filtradas com sucesso",
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
            )
    })
    @GetMapping("/filtrar")
    public ResponseEntity<PageResponseDTO<SecaoFolhetoDTO>> filtrar(
            @RequestParam(required = false) String palavra,
            @RequestParam(required = false) TipoSecao tipo,
            @RequestParam(required = false) TempoLit lit,
            Pageable pageable
    ){
        Page<SecaoFolhetoDTO> secoes = secaoService.filtrar(palavra, tipo, lit, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(secoes));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Cria uma nova seção")

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Seção criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecaoFolhetoDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso relacionado não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SecaoFolheto> criar(@RequestBody SecaoFolhetoDTO dto) {
        SecaoFolheto novaSecao = secaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSecao);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @Operation(summary = "Atualiza uma seção pelo ID")

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Seção atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecaoFolhetoDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Seção não encontrada",
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
    @PutMapping("/{id}")
    public ResponseEntity<SecaoFolheto> atualizar(
            @PathVariable Long id,
            @RequestBody SecaoFolhetoDTO dto) {

        SecaoFolheto secaoAtualizada = secaoService.atualizar(id, dto);
        return ResponseEntity.ok(secaoAtualizada);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Exclui uma seção pelo ID")

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "204",
                    description = "Seção excluída com sucesso",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Seção não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        secaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
