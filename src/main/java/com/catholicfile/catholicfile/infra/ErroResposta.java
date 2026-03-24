package com.catholicfile.catholicfile.infra;

import java.time.LocalDateTime;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensagem,
        String path
) {}