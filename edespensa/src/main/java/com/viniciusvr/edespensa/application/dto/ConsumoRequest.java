package com.viniciusvr.edespensa.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para requisição de consumo de um produto.
 */
public record ConsumoRequest(
        @NotBlank(message = "O ID do produto é obrigatório")
        String produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {}
