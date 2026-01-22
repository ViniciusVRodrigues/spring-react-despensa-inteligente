package com.viniciusvr.edespensa.application.usecase.pantry;

import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for discarding items from the pantry.
 */
public class DiscardPantryItemUseCase {

    private final PantryItemRepository pantryItemRepository;

    public DiscardPantryItemUseCase(PantryItemRepository pantryItemRepository) {
        this.pantryItemRepository = pantryItemRepository;
    }

    /**
     * Discard a single pantry item.
     * @return the product name of the discarded item
     */
    public String discardItem(Long pantryItemId) {
        PantryItem item = pantryItemRepository.findById(pantryItemId)
                .orElseThrow(() -> new EntityNotFoundException("PantryItem", pantryItemId));
        
        String productName = item.getProduct().getName();
        pantryItemRepository.delete(item);
        return productName;
    }

    /**
     * Discard multiple pantry items.
     * @return list of product names that were discarded
     */
    public List<String> discardItems(List<Long> pantryItemIds) {
        List<String> discardedNames = new ArrayList<>();
        
        for (Long id : pantryItemIds) {
            PantryItem item = pantryItemRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("PantryItem", id));
            discardedNames.add(item.getProduct().getName());
            pantryItemRepository.delete(item);
        }
        
        return discardedNames;
    }

    /**
     * Discard all expired items from the pantry.
     * @return list of product names that were discarded
     */
    public List<String> discardAllExpired() {
        List<PantryItem> allItems = pantryItemRepository.findAll();
        List<String> discardedNames = new ArrayList<>();
        
        for (PantryItem item : allItems) {
            if (item.isExpired()) {
                discardedNames.add(item.getProduct().getName());
                pantryItemRepository.delete(item);
            }
        }
        
        return discardedNames;
    }
}
