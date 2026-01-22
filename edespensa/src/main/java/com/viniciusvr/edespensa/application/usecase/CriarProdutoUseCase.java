package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ProdutoGateway;
import com.viniciusvr.edespensa.domain.entity.Produto;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para criar um novo produto na despensa.
 * Representa a ação de compra de um produto.
 */
@Service
public class CriarProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public CriarProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Executa a criação de um novo produto.
     * @param produto o produto a ser criado
     * @return o produto criado
     */
    public Produto executar(Produto produto) {
        return produtoGateway.salvar(produto);
    }
}
