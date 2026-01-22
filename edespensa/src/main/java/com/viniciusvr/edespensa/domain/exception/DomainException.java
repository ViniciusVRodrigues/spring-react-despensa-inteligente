package com.viniciusvr.edespensa.domain.exception;

/**
 * Base exception for domain-related errors.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
