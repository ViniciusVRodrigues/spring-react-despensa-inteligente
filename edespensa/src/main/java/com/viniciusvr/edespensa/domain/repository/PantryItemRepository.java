package com.viniciusvr.edespensa.domain.repository;

import com.viniciusvr.edespensa.domain.entity.PantryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface (port) for PantryItem entity.
 * This is a domain interface that will be implemented by infrastructure layer.
 */
public interface PantryItemRepository {

    PantryItem save(PantryItem pantryItem);

    Optional<PantryItem> findById(Long id);

    List<PantryItem> findAll();

    List<PantryItem> findByProductId(Long productId);

    List<PantryItem> findByProductName(String productName);

    List<PantryItem> findByExpirationDateBefore(LocalDate date);

    List<PantryItem> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    List<PantryItem> findByQuantityLessThanEqual(Double quantity);

    List<PantryItem> findExpiringSoon(int days);

    List<PantryItem> findLowStock(Double threshold);

    void deleteById(Long id);

    void delete(PantryItem pantryItem);

    boolean existsById(Long id);
}
