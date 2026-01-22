package com.viniciusvr.edespensa.domain.gateway;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;

import java.util.List;
import java.util.Optional;

/**
 * Interface Gateway para operações de persistência de ItemListaCompras.
 * Seguindo Clean Architecture, esta interface pertence ao domínio.
 */
public interface ItemListaComprasGateway {

    /**
     * Salva um item da lista de compras.
     * @param item o item a ser salvo
     * @return o item salvo
     */
    ItemListaCompras salvar(ItemListaCompras item);

    /**
     * Busca um item pelo ID.
     * @param id o ID do item
     * @return Optional contendo o item se encontrado
     */
    Optional<ItemListaCompras> buscarPorId(String id);

    /**
     * Lista todos os itens da lista de compras.
     * @return lista de itens
     */
    List<ItemListaCompras> listarTodos();

    /**
     * Lista itens não comprados.
     * @return lista de itens não comprados
     */
    List<ItemListaCompras> listarNaoComprados();

    /**
     * Deleta um item pelo ID.
     * @param id o ID do item
     */
    void deletar(String id);

    /**
     * Verifica se um item existe pelo ID.
     * @param id o ID do item
     * @return true se existir
     */
    boolean existePorId(String id);

    /**
     * Deleta todos os itens marcados como comprados.
     */
    void deletarComprados();

    /**
     * Salva uma lista de itens (batch).
     * @param itens lista de itens a serem salvos
     * @return lista de itens salvos
     */
    List<ItemListaCompras> salvarTodos(List<ItemListaCompras> itens);
}
