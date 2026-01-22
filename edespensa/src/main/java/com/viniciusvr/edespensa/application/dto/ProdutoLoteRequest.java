package com.viniciusvr.edespensa.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para requisição de cadastro em lote de produtos.
 */
public record ProdutoLoteRequest(
        @NotEmpty(message = "A lista de produtos não pode estar vazia")
        @Valid
        List<ProdutoRequest> produtos
) {}
