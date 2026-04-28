package com.catholicfile.catholicfile.controller;

import com.catholicfile.catholicfile.configurations.SecurityConfigurations;
import com.catholicfile.catholicfile.dtos.ErroDTO;
import com.catholicfile.catholicfile.dtos.PageResponseDTO;
import com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO;
import com.catholicfile.catholicfile.entities.SecaoFolheto;
import com.catholicfile.catholicfile.enums.TempoLit;
import com.catholicfile.catholicfile.enums.TipoSecao;
import com.catholicfile.catholicfile.services.SecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Erro de Filtro",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 400,
                  "error": "Bad Request",
                  "mensagem": "Parâmetro de busca 'nome' contém caracteres inválidos",
                  "path": "/secao/filtro"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Sem Permissão de Leitura",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Token inválido ou sem permissão para listar seções",
                  "path": "/secao/filtro"
                }
                """
                            )
                    )
            )
    })
    @GetMapping("/filtrar")
    public ResponseEntity<PageResponseDTO<SecaoFolhetoDTO>> filtrar(
            @RequestParam(required = false) String palavra,
            @RequestParam(required = false) TipoSecao tipo,
            @RequestParam(required = false) TempoLit lit,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable // Ajuste para paginação limpa no Swagger
    ){
        Page<SecaoFolhetoDTO> secoes = secaoService.filtrar(palavra, tipo, lit, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(secoes));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Cria uma nova seção")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Seção criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SecaoFolhetoDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Erro de Validação",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 400,
                  "error": "Bad Request",
                  "mensagem": "O conteúdo da seção não pode exceder o limite de caracteres",
                  "path": "/secao"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Sem Permissão",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Você não tem permissão para criar novas seções",
                  "path": "/secao"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso relacionado não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Folheto ou Categoria não existe",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Não foi possível vincular: recurso pai não encontrado",
                  "path": "/secao"
                }
                """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SecaoFolhetoDTO> criar(@RequestBody SecaoFolhetoDTO dto) {
        SecaoFolheto novaSecao = secaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SecaoFolhetoDTO(novaSecao));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Atualiza uma seção pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seção atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SecaoFolhetoDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Erro de Validação",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 400,
                  "error": "Bad Request",
                  "mensagem": "O campo 'titulo' da seção não pode exceder 100 caracteres",
                  "path": "/secao/1"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Sem Permissão de Escrita",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Você não tem autorização para editar esta seção",
                  "path": "/secao/1"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Seção não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Seção Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Não foi possível atualizar: Seção com ID 1 não encontrada",
                  "path": "/secao/1"
                }
                """
                            )
                    )
            )
    })
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<SecaoFolhetoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody SecaoFolhetoDTO dto) {

        SecaoFolheto secaoAtualizada = secaoService.atualizar(id, dto);
        return ResponseEntity.ok(new SecaoFolhetoDTO(secaoAtualizada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Exclui uma seção pelo ID", security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Seção excluída com sucesso",
                    content = @Content // No Content não retorna corpo
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Permissão Negada",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Você não possui privilégios para excluir esta seção",
                  "path": "/secao/1"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Seção não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Seção Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Não foi possível excluir: Seção com ID 1 não encontrada",
                  "path": "/secao/1"
                }
                """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        secaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}