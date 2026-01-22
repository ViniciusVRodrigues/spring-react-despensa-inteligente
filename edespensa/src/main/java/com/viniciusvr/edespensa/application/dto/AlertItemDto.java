package com.viniciusvr.edespensa.application.dto;

import java.time.LocalDate;

/**
 * DTO for an alert item displayed on dashboard.
 */
public record AlertItemDto(
    Long pantryItemId,
    Long productId,
    String productName,
    Double quantity,
    String unit,
    LocalDate expirationDate,
    String alertType,
    String alertMessage
) {}
