package com.viniciusvr.edespensa.application.dto;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para requisições de item da lista de compras.
 */
public record ItemListaComprasRequest(
        @NotBlank(message = "O nome do item é obrigatório")
        String nome,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        CategoriaProduto categoria
) {}
