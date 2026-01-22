package com.viniciusvr.edespensa.application.dto;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respostas de produto.
 */
public record ProdutoResponse(
        String id,
        String nome,
        Integer quantidade,
        LocalDate dataValidade,
        CategoriaProduto categoria,
        Boolean expirado,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    /**
     * Converte uma entidade Produto para ProdutoResponse.
     */
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getDataValidade(),
                produto.getCategoria(),
                produto.estaExpirado(),
                produto.getAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm()
        );
    }
}
