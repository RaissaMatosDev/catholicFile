package com.catholicFile.catholicFile.controller;

import com.catholicFile.catholicFile.DTOs.UsuarioAttDTO;
import com.catholicFile.catholicFile.DTOs.UsuarioDTO;
import com.catholicFile.catholicFile.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/usuarios")

public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController( UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid UsuarioDTO dados) {
        var usuarioSalvo = usuarioService.cadastrar(dados);

        return ResponseEntity
                .created(URI.create("/usuarios" + usuarioSalvo."/id()"))
                .body(usuarioSalvo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>>listar(){
        var usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioAttDTO dados) {
        var usuarioAtualizado = usuarioService.atualizar(id, dados);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();

    }

}
