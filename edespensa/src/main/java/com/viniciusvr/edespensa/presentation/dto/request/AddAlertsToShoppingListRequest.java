package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request DTO for adding multiple alert items to shopping list.
 */
public record AddAlertsToShoppingListRequest(
    @NotEmpty(message = "At least one pantry item ID is required")
    List<Long> pantryItemIds
) {}
