package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.gateway.ItemListaComprasGateway;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para adicionar um item à lista de compras.
 */
@Service
public class AdicionarItemListaComprasUseCase {

    private final ItemListaComprasGateway itemListaComprasGateway;

    public AdicionarItemListaComprasUseCase(ItemListaComprasGateway itemListaComprasGateway) {
        this.itemListaComprasGateway = itemListaComprasGateway;
    }

    /**
     * Executa a adição de um item à lista de compras.
     * @param item o item a ser adicionado
     * @return o item adicionado
     */
    public ItemListaCompras executar(ItemListaCompras item) {
        return itemListaComprasGateway.salvar(item);
    }
}
