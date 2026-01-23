package com.viniciusvr.edespensa.presentation.dto.response;

import com.viniciusvr.edespensa.domain.entity.Product;

/**
 * Response DTO for product data.
 */
public record ProductResponse(
    Long id,
    String name,
    String category,
    String unit,
    String description,
    boolean trackExpiration
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getUnit(),
            product.getDescription(),
            product.isTrackExpiration()
        );
    }
}
