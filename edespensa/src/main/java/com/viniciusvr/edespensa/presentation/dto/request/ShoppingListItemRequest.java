package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.Positive;

/**
 * Request DTO for adding an item to the shopping list.
 */
public record ShoppingListItemRequest(
    Long productId,
    
    String productName,
    
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    String priority,
    
    String notes
) {
    public boolean hasProductId() {
        return productId != null;
    }
    
    public boolean hasProductName() {
        return productName != null && !productName.isBlank();
    }
}
