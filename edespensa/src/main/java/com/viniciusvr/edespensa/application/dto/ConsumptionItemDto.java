package com.viniciusvr.edespensa.application.dto;

/**
 * DTO for consumption action containing item to consume and quantity.
 */
public record ConsumptionItemDto(
    Long pantryItemId,
    Double quantity
) {}
