package com.catholicFile.catholicFile.controller;


import com.catholicFile.catholicFile.DTOs.SecaoFolhetoDTO;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;
import com.catholicFile.catholicFile.services.SecaoService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secoes")

public class SecaoController {

    private final SecaoService secaoService;

    public SecaoController(SecaoService secaoService) {
        this.secaoService = secaoService;
    }

    @GetMapping
    public List<SecaoFolheto> listarSecoes(@PathVariable Long id) {
        return secaoService.buscarSecoesPorFolheto(id);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<SecaoFolheto>> filtrar(
            @RequestParam String palavra,
            @RequestParam(required = false) TipoSecao tipo,
            @RequestParam(required = false) TempoLit lit) {


        List<SecaoFolheto> secoes = secaoService.filtrarSecoes(palavra, tipo, lit);
        return ResponseEntity.ok(secoes);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<SecaoFolheto> criar(@RequestBody SecaoFolhetoDTO dto) {

        SecaoFolheto novaSecao = secaoService.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaSecao);
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<SecaoFolheto> atualizar(
            @PathVariable Long id,
            @RequestBody SecaoFolhetoDTO dto) {

        SecaoFolheto secaoAtualizada = secaoService.atualizar(id, dto);

        return ResponseEntity.ok(secaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        secaoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
