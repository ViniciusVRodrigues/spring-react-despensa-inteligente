package com.viniciusvr.edespensa.application.dto;

import java.time.LocalDate;

/**
 * DTO for consumption action containing item to consume and quantity.
 */
public record ConsumptionItemDto(
    Long pantryItemId,
    Double quantity
) {}
