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
    /**
     * Default expiring soon threshold in days (configurable via pantry.expiring-soon-days).
     */
    public static final int DEFAULT_EXPIRING_SOON_DAYS = 7;
    
    /**
     * Default low stock threshold (configurable via pantry.low-stock-threshold).
     */
    public static final double DEFAULT_LOW_STOCK_THRESHOLD = 2.0;

    public static PantryItemResponse fromDomain(PantryItem item) {
        return fromDomain(item, DEFAULT_EXPIRING_SOON_DAYS, DEFAULT_LOW_STOCK_THRESHOLD);
    }

    public static PantryItemResponse fromDomain(PantryItem item, int expiringSoonDays, double lowStockThreshold) {
        return new PantryItemResponse(
            item.getId(),
            ProductResponse.fromDomain(item.getProduct()),
            item.getQuantity(),
            item.getExpirationDate(),
            item.getAddedDate(),
            item.getLocation(),
            item.getNotes(),
            item.isExpired(),
            item.isExpiringSoon(expiringSoonDays),
            item.isLowStock(lowStockThreshold)
        );
    }
}
