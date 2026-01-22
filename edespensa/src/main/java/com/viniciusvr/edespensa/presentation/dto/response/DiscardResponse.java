package com.viniciusvr.edespensa.presentation.dto.response;

import java.util.List;

/**
 * Response DTO for discard operation result.
 */
public record DiscardResponse(
    int itemsDiscarded,
    List<String> discardedProductNames,
    String message
) {
    public static DiscardResponse single(String productName) {
        return new DiscardResponse(1, List.of(productName), 
            "Product '" + productName + "' was discarded successfully.");
    }
    
    public static DiscardResponse multiple(List<String> productNames) {
        return new DiscardResponse(productNames.size(), productNames, 
            productNames.size() + " item(s) discarded successfully.");
    }
}
