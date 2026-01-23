package com.viniciusvr.edespensa.domain.exception;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
