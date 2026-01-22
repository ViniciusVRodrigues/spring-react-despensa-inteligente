package com.viniciusvr.edespensa.domain.exception;

/**
 * Exceção lançada quando um item da lista de compras não é encontrado.
 */
public class ItemListaComprasNaoEncontradoException extends RuntimeException {

    public ItemListaComprasNaoEncontradoException(String id) {
        super("Item da lista de compras não encontrado com o ID: " + id);
    }

    public ItemListaComprasNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
