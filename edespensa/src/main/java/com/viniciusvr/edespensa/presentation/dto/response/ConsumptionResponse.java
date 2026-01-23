package com.viniciusvr.edespensa.presentation.dto.response;

import java.util.List;

/**
 * Response DTO for consumption operation result.
 */
public record ConsumptionResponse(
    int itemsConsumed,
    List<Long> depletedItemIds,
    List<String> depletedProductNames,
    String message,
    boolean hasDepletedItems
) {
    public static ConsumptionResponse from(int itemsConsumed, List<Long> depletedItemIds, 
                                           List<String> depletedProductNames, String message) {
        return new ConsumptionResponse(
            itemsConsumed,
            depletedItemIds,
            depletedProductNames,
            message,
            !depletedItemIds.isEmpty()
        );
    }
}
