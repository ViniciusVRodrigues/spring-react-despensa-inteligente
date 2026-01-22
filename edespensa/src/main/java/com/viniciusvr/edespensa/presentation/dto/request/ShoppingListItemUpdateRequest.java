package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.Positive;

/**
 * Request DTO for updating a shopping list item.
 */
public record ShoppingListItemUpdateRequest(
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    String priority,
    
    String notes
) {}
