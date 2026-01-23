package com.viniciusvr.edespensa.application.usecase.pantry;

import com.viniciusvr.edespensa.application.dto.BatchConsumptionDto;
import com.viniciusvr.edespensa.application.dto.ConsumptionItemDto;
import com.viniciusvr.edespensa.application.dto.ConsumptionResultDto;
import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.exception.InsufficientQuantityException;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for consuming items from the pantry.
 * Supports batch consumption and provides feedback about depleted items.
 */
public class ConsumePantryItemUseCase {

    private final PantryItemRepository pantryItemRepository;

    public ConsumePantryItemUseCase(PantryItemRepository pantryItemRepository) {
        this.pantryItemRepository = pantryItemRepository;
    }

    /**
     * Consume a single item from the pantry.
     */
    public ConsumptionResultDto consumeItem(Long pantryItemId, Double quantity) {
        PantryItem item = pantryItemRepository.findById(pantryItemId)
                .orElseThrow(() -> new EntityNotFoundException("PantryItem", pantryItemId));
        
        if (quantity > item.getQuantity()) {
            throw new InsufficientQuantityException(
                item.getProduct().getName(), 
                quantity, 
                item.getQuantity()
            );
        }
        
        List<Long> depletedIds = new ArrayList<>();
        List<String> depletedNames = new ArrayList<>();
        
        boolean isEmpty = item.consume(quantity);
        
        if (isEmpty) {
            depletedIds.add(item.getId());
            depletedNames.add(item.getProduct().getName());
            pantryItemRepository.delete(item);
        } else {
            pantryItemRepository.save(item);
        }
        
        return ConsumptionResultDto.success(1, depletedIds, depletedNames);
    }

    /**
     * Consume multiple items in batch.
     */
    public ConsumptionResultDto consumeBatch(BatchConsumptionDto batchDto) {
        List<Long> depletedIds = new ArrayList<>();
        List<String> depletedNames = new ArrayList<>();
        int consumedCount = 0;
        
        for (ConsumptionItemDto consumptionItem : batchDto.items()) {
            PantryItem item = pantryItemRepository.findById(consumptionItem.pantryItemId())
                    .orElseThrow(() -> new EntityNotFoundException("PantryItem", consumptionItem.pantryItemId()));
            
            if (consumptionItem.quantity() > item.getQuantity()) {
                throw new InsufficientQuantityException(
                    item.getProduct().getName(), 
                    consumptionItem.quantity(), 
                    item.getQuantity()
                );
            }
            
            boolean isEmpty = item.consume(consumptionItem.quantity());
            
            if (isEmpty) {
                depletedIds.add(item.getId());
                depletedNames.add(item.getProduct().getName());
                pantryItemRepository.delete(item);
            } else {
                pantryItemRepository.save(item);
            }
            
            consumedCount++;
        }
        
        return ConsumptionResultDto.success(consumedCount, depletedIds, depletedNames);
    }
}
