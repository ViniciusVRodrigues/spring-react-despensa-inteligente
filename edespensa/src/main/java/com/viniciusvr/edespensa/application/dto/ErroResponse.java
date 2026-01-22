package com.viniciusvr.edespensa.application.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respostas de erro da API.
 */
public record ErroResponse(
        int status,
        String mensagem,
        LocalDateTime timestamp,
        List<String> erros
) {
    public ErroResponse(int status, String mensagem) {
        this(status, mensagem, LocalDateTime.now(), null);
    }

    public ErroResponse(int status, String mensagem, List<String> erros) {
        this(status, mensagem, LocalDateTime.now(), erros);
    }
}
