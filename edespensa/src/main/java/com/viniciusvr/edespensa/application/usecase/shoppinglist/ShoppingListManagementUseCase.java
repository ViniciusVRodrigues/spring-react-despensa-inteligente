package com.viniciusvr.edespensa.application.usecase.shoppinglist;

import com.viniciusvr.edespensa.domain.entity.Product;
import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.repository.ProductRepository;
import com.viniciusvr.edespensa.domain.repository.ShoppingListItemRepository;

import java.util.List;

/**
 * Use case for managing shopping list items.
 */
public class ShoppingListManagementUseCase {

    private final ShoppingListItemRepository shoppingListRepository;
    private final ProductRepository productRepository;

    public ShoppingListManagementUseCase(ShoppingListItemRepository shoppingListRepository, 
                                          ProductRepository productRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.productRepository = productRepository;
    }

    /**
     * Add an item to the shopping list using an existing product.
     */
    public ShoppingListItem addItem(Long productId, Double quantity, ShoppingListItem.Priority priority, String notes) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));
        
        // Check if already in pending list - update quantity instead
        return shoppingListRepository.findPendingByProductId(productId)
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    if (priority != null && priority.ordinal() > existingItem.getPriority().ordinal()) {
                        existingItem.setPriority(priority);
                    }
                    return shoppingListRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    ShoppingListItem item = ShoppingListItem.createNew(product, quantity, 
                            priority != null ? priority : ShoppingListItem.Priority.MEDIUM, notes);
                    return shoppingListRepository.save(item);
                });
    }

    /**
     * Add an item to the shopping list by product name - creates product if it doesn't exist.
     */
    public ShoppingListItem addItemByName(String productName, Double quantity, ShoppingListItem.Priority priority, String notes) {
        Product product = productRepository.findByName(productName)
                .orElseGet(() -> {
                    Product newProduct = Product.createNew(productName, "Outros", "un", null, false);
                    return productRepository.save(newProduct);
                });
        
        return addItem(product.getId(), quantity, priority, notes);
    }

    /**
     * Add items automatically due to running low or expiring.
     */
    public ShoppingListItem addAutoItem(Long productId, Double suggestedQuantity, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));
        
        // Check if already exists
        if (shoppingListRepository.existsPendingByProductId(productId)) {
            return shoppingListRepository.findPendingByProductId(productId).orElseThrow();
        }
        
        ShoppingListItem item = ShoppingListItem.createAutoAdded(product, suggestedQuantity, reason);
        return shoppingListRepository.save(item);
    }

    public ShoppingListItem getItemById(Long id) {
        return shoppingListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ShoppingListItem", id));
    }

    public List<ShoppingListItem> getAllItems() {
        return shoppingListRepository.findAll();
    }

    public List<ShoppingListItem> getPendingItems() {
        return shoppingListRepository.findPendingItems();
    }

    public ShoppingListItem updateItem(Long id, Double quantity, ShoppingListItem.Priority priority, String notes) {
        ShoppingListItem item = getItemById(id);
        
        if (quantity != null) item.setQuantity(quantity);
        if (priority != null) item.setPriority(priority);
        if (notes != null) item.setNotes(notes);
        
        return shoppingListRepository.save(item);
    }

    public void markAsPurchased(Long id) {
        ShoppingListItem item = getItemById(id);
        item.markAsPurchased();
        shoppingListRepository.save(item);
    }

    public void markAsPurchasedBatch(List<Long> ids) {
        for (Long id : ids) {
            markAsPurchased(id);
        }
    }

    public void cancelItem(Long id) {
        ShoppingListItem item = getItemById(id);
        item.cancel();
        shoppingListRepository.save(item);
    }

    public void deleteItem(Long id) {
        if (!shoppingListRepository.existsById(id)) {
            throw new EntityNotFoundException("ShoppingListItem", id);
        }
        shoppingListRepository.deleteById(id);
    }

    public void clearPurchasedItems() {
        List<ShoppingListItem> purchased = shoppingListRepository.findByStatus(ShoppingListItem.Status.PURCHASED);
        for (ShoppingListItem item : purchased) {
            shoppingListRepository.delete(item);
        }
    }
}
