package com.catholicFile.catholicFile.infra;

import java.time.LocalDateTime;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String mensagem
) {}