package com.viniciusvr.edespensa.presentation.dto.response;

import com.viniciusvr.edespensa.domain.entity.PantryItem;
import java.time.LocalDate;

/**
 * Response DTO for pantry item data.
 */
public record PantryItemResponse(
    Long id,
    ProductResponse product,
    Double quantity,
    LocalDate expirationDate,
    LocalDate addedDate,
    String location,
    String notes,
    boolean isExpired,
    boolean isExpiringSoon,
    boolean isLowStock
) {
    public static PantryItemResponse fromDomain(PantryItem item) {
        return new PantryItemResponse(
            item.getId(),
            ProductResponse.fromDomain(item.getProduct()),
            item.getQuantity(),
            item.getExpirationDate(),
            item.getAddedDate(),
            item.getLocation(),
            item.getNotes(),
            item.isExpired(),
            item.isExpiringSoon(7),
            item.isLowStock(2.0)
        );
    }
}
