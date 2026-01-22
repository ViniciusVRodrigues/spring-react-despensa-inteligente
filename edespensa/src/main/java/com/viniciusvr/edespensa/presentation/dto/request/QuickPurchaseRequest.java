package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Request DTO for quick purchase action.
 * Either productId OR productName must be provided.
 */
public record QuickPurchaseRequest(
    Long productId,
    
    String productName,
    
    String category,
    
    String unit,
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    LocalDate expirationDate,
    
    String location,
    
    String notes
) {
    public boolean hasProductId() {
        return productId != null;
    }
    
    public boolean hasProductName() {
        return productName != null && !productName.isBlank();
    }
}
