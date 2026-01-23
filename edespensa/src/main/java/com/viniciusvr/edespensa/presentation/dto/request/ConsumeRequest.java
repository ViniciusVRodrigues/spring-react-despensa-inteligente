package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for consuming a single pantry item.
 */
public record ConsumeRequest(
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    Double quantity
) {}
