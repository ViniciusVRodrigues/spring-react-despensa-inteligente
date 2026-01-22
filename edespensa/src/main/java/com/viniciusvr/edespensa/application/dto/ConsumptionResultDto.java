package com.viniciusvr.edespensa.application.dto;

import java.util.List;

/**
 * DTO for the result of a consumption operation.
 */
public record ConsumptionResultDto(
    int itemsConsumed,
    List<Long> depletedItemIds,
    List<String> depletedProductNames,
    String message
) {
    public static ConsumptionResultDto success(int itemsConsumed, List<Long> depletedItemIds, List<String> depletedProductNames) {
        String msg = itemsConsumed + " item(s) consumed successfully.";
        if (!depletedProductNames.isEmpty()) {
            msg += " Products depleted: " + String.join(", ", depletedProductNames) + 
                   ". Would you like to add them to your shopping list?";
        }
        return new ConsumptionResultDto(itemsConsumed, depletedItemIds, depletedProductNames, msg);
    }
}
