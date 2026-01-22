package com.viniciusvr.edespensa.application.dto;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;

import java.time.LocalDateTime;

/**
 * DTO para respostas de item da lista de compras.
 */
public record ItemListaComprasResponse(
        String id,
        String nome,
        Integer quantidade,
        CategoriaProduto categoria,
        Boolean comprado,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    /**
     * Converte uma entidade ItemListaCompras para ItemListaComprasResponse.
     */
    public static ItemListaComprasResponse fromEntity(ItemListaCompras item) {
        return new ItemListaComprasResponse(
                item.getId(),
                item.getNome(),
                item.getQuantidade(),
                item.getCategoria(),
                item.getComprado(),
                item.getCriadoEm(),
                item.getAtualizadoEm()
        );
    }
}
