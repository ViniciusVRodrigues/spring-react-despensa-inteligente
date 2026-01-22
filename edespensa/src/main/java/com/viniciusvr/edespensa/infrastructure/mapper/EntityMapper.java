package com.viniciusvr.edespensa.infrastructure.mapper;

import com.viniciusvr.edespensa.application.dto.ItemListaComprasRequest;
import com.viniciusvr.edespensa.application.dto.ProdutoRequest;
import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.entity.Produto;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre DTOs e entidades.
 */
@Component
public class EntityMapper {

    /**
     * Converte ProdutoRequest para entidade Produto.
     */
    public Produto toEntity(ProdutoRequest request) {
        return new Produto(
                request.nome(),
                request.quantidade(),
                request.dataValidade(),
                request.categoria()
        );
    }

    /**
     * Converte ItemListaComprasRequest para entidade ItemListaCompras.
     */
    public ItemListaCompras toEntity(ItemListaComprasRequest request) {
        return new ItemListaCompras(
                request.nome(),
                request.quantidade(),
                request.categoria()
        );
    }
}
