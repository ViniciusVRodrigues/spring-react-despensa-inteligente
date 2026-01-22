package com.viniciusvr.edespensa.presentation.dto.response;

import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import java.time.LocalDateTime;

/**
 * Response DTO for shopping list item data.
 */
public record ShoppingListItemResponse(
    Long id,
    ProductResponse product,
    Double quantity,
    String priority,
    String status,
    LocalDateTime addedAt,
    String notes,
    boolean autoAdded
) {
    public static ShoppingListItemResponse fromDomain(ShoppingListItem item) {
        return new ShoppingListItemResponse(
            item.getId(),
            ProductResponse.fromDomain(item.getProduct()),
            item.getQuantity(),
            item.getPriority().name(),
            item.getStatus().name(),
            item.getAddedAt(),
            item.getNotes(),
            item.isAutoAdded()
        );
    }
}
