package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvr.edespensa.domain.gateway.ProdutoGateway;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para descartar (soft delete) um produto.
 */
@Service
public class DescartarProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public DescartarProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Executa o descarte de um produto (soft delete).
     * @param id o ID do produto a ser descartado
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     */
    public void executar(String id) {
        Produto produto = produtoGateway.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.descartar();
        produtoGateway.salvar(produto);
    }
}
