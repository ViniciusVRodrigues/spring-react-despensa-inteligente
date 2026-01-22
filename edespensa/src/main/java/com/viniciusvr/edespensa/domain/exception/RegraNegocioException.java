package com.viniciusvr.edespensa.domain.exception;

/**
 * Exceção lançada quando há regras de negócio violadas.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
