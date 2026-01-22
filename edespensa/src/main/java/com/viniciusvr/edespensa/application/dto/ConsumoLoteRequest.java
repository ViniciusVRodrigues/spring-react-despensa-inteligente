package com.viniciusvr.edespensa.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para requisição de consumo em lote.
 */
public record ConsumoLoteRequest(
        @NotEmpty(message = "A lista de consumos não pode estar vazia")
        @Valid
        List<ConsumoRequest> consumos
) {}
