package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.gateway.ItemListaComprasGateway;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para listar itens da lista de compras.
 */
@Service
public class ListarItensListaComprasUseCase {

    private final ItemListaComprasGateway itemListaComprasGateway;

    public ListarItensListaComprasUseCase(ItemListaComprasGateway itemListaComprasGateway) {
        this.itemListaComprasGateway = itemListaComprasGateway;
    }

    /**
     * Lista todos os itens da lista de compras.
     * @return lista de itens
     */
    public List<ItemListaCompras> executar() {
        return itemListaComprasGateway.listarTodos();
    }

    /**
     * Lista apenas os itens não comprados.
     * @return lista de itens não comprados
     */
    public List<ItemListaCompras> listarNaoComprados() {
        return itemListaComprasGateway.listarNaoComprados();
    }
}
