package com.catholicFile.catholicFile.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> tratarErroDuplicidade(DataIntegrityViolationException ex) {

        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflito entre tratamento de dados (ERRO 409)"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}