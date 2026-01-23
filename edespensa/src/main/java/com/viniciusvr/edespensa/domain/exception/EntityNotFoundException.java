package com.viniciusvr.edespensa.domain.exception;

/**
 * Exception thrown when a requested entity is not found.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s with id %d was not found", entityName, id));
    }

    public EntityNotFoundException(String entityName, String identifier) {
        super(String.format("%s with identifier '%s' was not found", entityName, identifier));
    }
}
