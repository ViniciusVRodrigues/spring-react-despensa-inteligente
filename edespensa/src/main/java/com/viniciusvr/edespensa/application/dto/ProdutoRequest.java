package com.viniciusvr.edespensa.application.dto;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para requisições de criação/atualização de produto.
 */
public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório")
        String nome,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 0, message = "A quantidade deve ser maior ou igual a zero")
        Integer quantidade,

        LocalDate dataValidade,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaProduto categoria
) {}
