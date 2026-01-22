package com.viniciusvr.edespensa.domain.gateway;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;

import java.util.List;
import java.util.Optional;

/**
 * Interface Gateway para operações de persistência de Produto.
 * Seguindo Clean Architecture, esta interface pertence ao domínio.
 */
public interface ProdutoGateway {

    /**
     * Salva um produto.
     * @param produto o produto a ser salvo
     * @return o produto salvo
     */
    Produto salvar(Produto produto);

    /**
     * Busca um produto pelo ID.
     * @param id o ID do produto
     * @return Optional contendo o produto se encontrado
     */
    Optional<Produto> buscarPorId(String id);

    /**
     * Lista todos os produtos ativos.
     * @return lista de produtos ativos
     */
    List<Produto> listarTodosAtivos();

    /**
     * Lista produtos por categoria.
     * @param categoria a categoria dos produtos
     * @return lista de produtos da categoria
     */
    List<Produto> listarPorCategoria(CategoriaProduto categoria);

    /**
     * Lista produtos com estoque baixo.
     * @param limiar o limite para considerar estoque baixo
     * @return lista de produtos com estoque baixo
     */
    List<Produto> listarComEstoqueBaixo(Integer limiar);

    /**
     * Lista produtos expirados.
     * @return lista de produtos expirados
     */
    List<Produto> listarExpirados();

    /**
     * Deleta um produto pelo ID.
     * @param id o ID do produto
     */
    void deletar(String id);

    /**
     * Verifica se um produto existe pelo ID.
     * @param id o ID do produto
     * @return true se existir
     */
    boolean existePorId(String id);

    /**
     * Salva uma lista de produtos (batch).
     * @param produtos lista de produtos a serem salvos
     * @return lista de produtos salvos
     */
    List<Produto> salvarTodos(List<Produto> produtos);
}
