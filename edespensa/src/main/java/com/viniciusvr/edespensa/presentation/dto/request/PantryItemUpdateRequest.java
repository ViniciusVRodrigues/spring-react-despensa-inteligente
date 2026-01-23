package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Request DTO for updating a pantry item.
 */
public record PantryItemUpdateRequest(
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    LocalDate expirationDate,
    
    String location,
    
    String notes
) {}
