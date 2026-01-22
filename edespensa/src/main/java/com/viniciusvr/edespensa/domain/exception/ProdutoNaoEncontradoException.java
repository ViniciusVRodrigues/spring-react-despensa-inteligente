package com.viniciusvr.edespensa.domain.exception;

/**
 * Exceção lançada quando um produto não é encontrado.
 */
public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(String id) {
        super("Produto não encontrado com o ID: " + id);
    }

    public ProdutoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
