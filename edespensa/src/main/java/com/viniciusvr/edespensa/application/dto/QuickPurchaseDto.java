package com.viniciusvr.edespensa.application.dto;

import java.time.LocalDate;

/**
 * DTO for quick purchase action to add items to pantry.
 */
public record QuickPurchaseDto(
    Long productId,
    String productName,
    String category,
    String unit,
    Double quantity,
    LocalDate expirationDate,
    String location,
    String notes
) {
    public boolean hasExistingProduct() {
        return productId != null;
    }
}
