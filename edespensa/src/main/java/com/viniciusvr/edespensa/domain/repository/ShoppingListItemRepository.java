package com.viniciusvr.edespensa.domain.repository;

import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface (port) for ShoppingListItem entity.
 * This is a domain interface that will be implemented by infrastructure layer.
 */
public interface ShoppingListItemRepository {

    ShoppingListItem save(ShoppingListItem item);

    Optional<ShoppingListItem> findById(Long id);

    List<ShoppingListItem> findAll();

    List<ShoppingListItem> findByStatus(ShoppingListItem.Status status);

    List<ShoppingListItem> findPendingItems();

    List<ShoppingListItem> findByProductId(Long productId);

    Optional<ShoppingListItem> findPendingByProductId(Long productId);

    void deleteById(Long id);

    void delete(ShoppingListItem item);

    boolean existsById(Long id);

    boolean existsPendingByProductId(Long productId);
}
