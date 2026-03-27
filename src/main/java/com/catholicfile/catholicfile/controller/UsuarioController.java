package com.catholicfile.catholicfile.controller;

import com.catholicfile.catholicfile.configurations.SecurityConfigurations;
import com.catholicfile.catholicfile.dtos.*;
import com.catholicfile.catholicfile.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário", description = "Endpoints para gerenciamento de usuários e autenticação")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Login do usuário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Senha Incorreta",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 401,
                  "error": "Unauthorized",
                  "mensagem": "E-mail ou senha inválidos",
                  "path": "/login"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Usuário não cadastrado no sistema",
                  "path": "/login"
                }
                """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dados) {
        String token = usuarioService.autenticar(dados.email(), dados.senha());
        return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer"));
    }

    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Cadastra um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))
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
                  "mensagem": "E-mail já cadastrado ou formato de senha inválido",
                  "path": "/usuarios"
                }
                """
                            )
                    )
            )
    })
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDTO> cadastrar(
            @RequestBody @Valid UsuarioCadastroDTO dados,
            @AuthenticationPrincipal UserDetails criador) {
        UsuarioDTO usuarioSalvo = usuarioService.cadastrar(dados, criador);
        return ResponseEntity
                .created(URI.create("/usuarios/" + usuarioSalvo.id()))
                .body(usuarioSalvo);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Atualiza o perfil do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))
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
                  "mensagem": "Você não tem permissão para alterar os dados de outro usuário",
                  "path": "/usuarios/perfil"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "O usuário solicitado não foi encontrado na base de dados",
                  "path": "/usuarios/perfil"
                }
                """
                            )
                    )
            )
    })
    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO> atualizarMeuPerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UsuarioAttDTO dto) {
        var atualizado = usuarioService.atualizarMeuPerfil(dto, userDetails.getUsername());
        return ResponseEntity.ok(atualizado);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Lista todos os usuários (Admin apenas)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)) // Ajuste para a classe da sua lista
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Permissão Insuficiente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Apenas administradores podem listar todos os registros",
                  "path": "/usuarios"
                }
                """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listar(
            @ParameterObject
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        var page = usuarioService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Atualiza um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Sem Permissão Administrativa",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 403,
                  "error": "Forbidden",
                  "mensagem": "Você não tem permissão para editar outros usuários",
                  "path": "/usuarios/1"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Não foi possível atualizar: usuário com ID 1 não encontrado",
                  "path": "/usuarios/1"
                }
                """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioAttDTO dados) {
        var usuarioAtualizado = usuarioService.atualizarPorId(id, dados);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @Operation(summary = "Exclui um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário excluído com sucesso",
                    content = @Content // Sem corpo para No Content
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
                  "mensagem": "Apenas administradores podem excluir usuários",
                  "path": "/usuarios/1"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário Inexistente",
                                    value = """
                {
                  "timestamp": "2026-03-27T12:00:00Z",
                  "status": 404,
                  "error": "Not Found",
                  "mensagem": "Não foi possível excluir: ID informado não existe",
                  "path": "/usuarios/1"
                }
                """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
