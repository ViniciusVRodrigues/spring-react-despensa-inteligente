package com.viniciusvr.edespensa.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a product.
 */
public record ProductRequest(
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    String name,
    
    @NotBlank(message = "Category is required")
    String category,
    
    @NotBlank(message = "Unit is required")
    String unit,
    
    String description,
    
    boolean trackExpiration
) {}
