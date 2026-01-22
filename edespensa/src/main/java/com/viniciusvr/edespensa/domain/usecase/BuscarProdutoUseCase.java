package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvr.edespensa.domain.gateway.ProdutoGateway;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para buscar produtos da despensa.
 */
@Service
public class BuscarProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public BuscarProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Busca um produto pelo ID.
     * @param id o ID do produto
     * @return o produto encontrado
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     */
    public Produto buscarPorId(String id) {
        return produtoGateway.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    /**
     * Lista todos os produtos ativos da despensa.
     * @return lista de produtos ativos
     */
    public List<Produto> listarTodosAtivos() {
        return produtoGateway.listarTodosAtivos();
    }

    /**
     * Lista produtos por categoria.
     * @param categoria a categoria dos produtos
     * @return lista de produtos da categoria
     */
    public List<Produto> listarPorCategoria(CategoriaProduto categoria) {
        return produtoGateway.listarPorCategoria(categoria);
    }

    /**
     * Lista produtos com estoque baixo.
     * @param limiar o limite para considerar estoque baixo
     * @return lista de produtos com estoque baixo
     */
    public List<Produto> listarComEstoqueBaixo(Integer limiar) {
        return produtoGateway.listarComEstoqueBaixo(limiar);
    }

    /**
     * Lista produtos expirados.
     * @return lista de produtos expirados
     */
    public List<Produto> listarExpirados() {
        return produtoGateway.listarExpirados();
    }
}
