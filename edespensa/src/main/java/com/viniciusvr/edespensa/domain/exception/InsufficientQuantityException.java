package com.viniciusvr.edespensa.domain.exception;

/**
 * Exception thrown when there is insufficient quantity to perform an operation.
 */
public class InsufficientQuantityException extends DomainException {

    public InsufficientQuantityException(String productName, Double requested, Double available) {
        super(String.format("Insufficient quantity for '%s': requested %.2f but only %.2f available", 
                            productName, requested, available));
    }
}
