package com.catholicfile.catholicfile.infra;
import com.catholicfile.catholicfile.dtos.ErroDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> tratarErroDuplicidade(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                "Violação de integridade de dados",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> tratarNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request) {
        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroDTO> tratarResponsestatus(ResponseStatusException ex, HttpServletRequest request) {
        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(erro);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErroDTO> tratarErroAcessoNegado(
            org.springframework.security.access.AccessDeniedException ex,
            HttpServletRequest request) {

        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "FORBIDDEN",
                "Acesso negado: você não tem permissão para acessar este recurso.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErroDTO> handleNullPointer(NullPointerException ex, HttpServletRequest request) {
        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
//Se algum campo do DTO estiver invalido, retorna a mensagem correta sem gambiarras
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensagens = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce("", (a,b) -> a + b + "; ");

        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                mensagens,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErroDTO> tratarRotaNaoEncontrada(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        ErroDTO erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                "Rota não encontrada: " + ex.getRequestURL(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
    }
