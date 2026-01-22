package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ItemListaComprasGateway;
import com.viniciusvr.edespensa.domain.exception.ItemListaComprasNaoEncontradoException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para remover um item da lista de compras.
 */
@Service
public class RemoverItemListaComprasUseCase {

    private final ItemListaComprasGateway itemListaComprasGateway;

    public RemoverItemListaComprasUseCase(ItemListaComprasGateway itemListaComprasGateway) {
        this.itemListaComprasGateway = itemListaComprasGateway;
    }

    /**
     * Executa a remoção de um item da lista de compras.
     * @param id o ID do item a ser removido
     * @throws ItemListaComprasNaoEncontradoException se o item não for encontrado
     */
    public void executar(String id) {
        if (!itemListaComprasGateway.existePorId(id)) {
            throw new ItemListaComprasNaoEncontradoException(id);
        }
        itemListaComprasGateway.deletar(id);
    }
}
