package com.catholicFile.catholicFile.controller;


import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.infra.RecursoNaoEncontradoException;
import com.catholicFile.catholicFile.services.FolhetoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) throws Exception {

        byte[] pdf = folhetoService.gerarPdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // forma correta
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=folheto.pdf")
                .body(pdf);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<FolhetoDTO> cadastrarFolheto(@RequestBody @Valid FolhetoDTO dados) throws RecursoNaoEncontradoException {
        var folhetoSalvo = folhetoService.cadastrarFolheto(dados);

        return ResponseEntity
                .created(URI.create("/folheto/" + folhetoSalvo.id()))
                .body(folhetoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFolheto(@PathVariable Long id) throws RecursoNaoEncontradoException {
        folhetoService.excluir(id);
        return ResponseEntity.noContent().build();


    }
}
