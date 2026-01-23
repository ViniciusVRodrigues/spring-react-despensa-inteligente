package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Request DTO for adding an item to the pantry.
 */
public record PantryItemRequest(
    @NotNull(message = "Product ID is required")
    Long productId,
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    LocalDate expirationDate,
    
    String location,
    
    String notes
) {}
