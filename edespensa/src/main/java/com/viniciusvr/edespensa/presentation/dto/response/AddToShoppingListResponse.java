package com.viniciusvr.edespensa.presentation.dto.response;

/**
 * Response DTO for add to shopping list operation.
 */
public record AddToShoppingListResponse(
    int itemsAdded,
    String message
) {
    public static AddToShoppingListResponse success(int itemsAdded) {
        return new AddToShoppingListResponse(itemsAdded, 
            itemsAdded + " item(s) added to shopping list successfully.");
    }
}
