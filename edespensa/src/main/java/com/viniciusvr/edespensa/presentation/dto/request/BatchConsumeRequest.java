package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Request DTO for batch consumption of multiple pantry items.
 */
public record BatchConsumeRequest(
    @NotEmpty(message = "At least one item is required")
    @Valid
    List<ConsumeItem> items
) {
    public record ConsumeItem(
        @NotNull(message = "Pantry item ID is required")
        Long pantryItemId,
        
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Double quantity
    ) {}
}
