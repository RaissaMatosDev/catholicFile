package com.catholicFile.catholicFile.controller;

import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;


@RestController
@RequestMapping ("/usuarios")

public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController( UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");
        String token = usuarioService.autenticar(email, senha);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid UsuarioDTO dados) {
        var usuarioSalvo = usuarioService.cadastrar(dados);

        return ResponseEntity
                .created(URI.create("/usuarios/" + usuarioSalvo.id()))
                .body(usuarioSalvo);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable){

        var page = usuarioService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioAttDTO dados) {
        var usuarioAtualizado = usuarioService.atualizar(dados);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();

    }

}
