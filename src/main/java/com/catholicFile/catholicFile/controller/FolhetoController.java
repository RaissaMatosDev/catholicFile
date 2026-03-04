package com.catholicFile.catholicFile.controller;


import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.services.FolhetoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/folheto")

public class FolhetoController {


    private final FolhetoService folhetoService;

    public FolhetoController(FolhetoService folhetoService) {
        this.folhetoService = folhetoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<FolhetoDTO> cadastrarFolheto(@RequestBody @Valid FolhetoDTO dados) {
        var folhetoSalvo = folhetoService.cadastrarFolheto(dados);

        return ResponseEntity
                .created(URI.create("/folheto/" + folhetoSalvo.id()))
                .body(folhetoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFolheto(@PathVariable Long id) {
        folhetoService.excluir(id);
        return ResponseEntity.noContent().build();


    }
}
