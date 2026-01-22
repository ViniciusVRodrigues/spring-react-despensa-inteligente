package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ProdutoGateway;
import com.viniciusvr.edespensa.domain.entity.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para criar produtos em lote (batch).
 */
@Service
public class CriarProdutosEmLoteUseCase {

    private final ProdutoGateway produtoGateway;

    public CriarProdutosEmLoteUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Executa a criação de produtos em lote.
     * @param produtos lista de produtos a serem criados
     * @return lista de produtos criados
     */
    public List<Produto> executar(List<Produto> produtos) {
        return produtoGateway.salvarTodos(produtos);
    }
}
