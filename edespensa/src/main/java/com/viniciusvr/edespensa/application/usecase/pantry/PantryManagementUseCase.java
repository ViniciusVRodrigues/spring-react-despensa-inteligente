package com.viniciusvr.edespensa.application.usecase.pantry;

import com.viniciusvr.edespensa.application.dto.QuickPurchaseDto;
import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.domain.entity.Product;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;
import com.viniciusvr.edespensa.domain.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Use case for managing pantry items.
 */
public class PantryManagementUseCase {

    private final PantryItemRepository pantryItemRepository;
    private final ProductRepository productRepository;

    public PantryManagementUseCase(PantryItemRepository pantryItemRepository, ProductRepository productRepository) {
        this.pantryItemRepository = pantryItemRepository;
        this.productRepository = productRepository;
    }

    public PantryItem addToPantry(Long productId, Double quantity, LocalDate expirationDate, String location, String notes) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));
        
        PantryItem pantryItem = PantryItem.createNew(product, quantity, expirationDate, location, notes);
        return pantryItemRepository.save(pantryItem);
    }

    /**
     * Quick purchase action - creates product if needed and adds to pantry.
     */
    public PantryItem quickPurchase(QuickPurchaseDto purchaseDto) {
        Product product;
        
        if (purchaseDto.hasExistingProduct()) {
            product = productRepository.findById(purchaseDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product", purchaseDto.productId()));
        } else {
            // Create new product if it doesn't exist
            product = productRepository.findByName(purchaseDto.productName())
                    .orElseGet(() -> {
                        Product newProduct = Product.createNew(
                            purchaseDto.productName(),
                            purchaseDto.category() != null ? purchaseDto.category() : "Outros",
                            purchaseDto.unit() != null ? purchaseDto.unit() : "un",
                            null,
                            purchaseDto.expirationDate() != null
                        );
                        return productRepository.save(newProduct);
                    });
        }
        
        PantryItem pantryItem = PantryItem.createNew(
            product, 
            purchaseDto.quantity(), 
            purchaseDto.expirationDate(),
            purchaseDto.location(),
            purchaseDto.notes()
        );
        
        return pantryItemRepository.save(pantryItem);
    }

    public PantryItem getPantryItemById(Long id) {
        return pantryItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PantryItem", id));
    }

    public List<PantryItem> getAllPantryItems() {
        return pantryItemRepository.findAll();
    }

    public List<PantryItem> getPantryItemsByProduct(Long productId) {
        return pantryItemRepository.findByProductId(productId);
    }

    public List<PantryItem> searchPantryItemsByProductName(String productName) {
        return pantryItemRepository.findByProductName(productName);
    }

    public PantryItem updatePantryItem(Long id, Double quantity, LocalDate expirationDate, String location, String notes) {
        PantryItem item = getPantryItemById(id);
        
        if (quantity != null) item.setQuantity(quantity);
        if (expirationDate != null) item.setExpirationDate(expirationDate);
        if (location != null) item.setLocation(location);
        if (notes != null) item.setNotes(notes);
        
        return pantryItemRepository.save(item);
    }

    public void deletePantryItem(Long id) {
        if (!pantryItemRepository.existsById(id)) {
            throw new EntityNotFoundException("PantryItem", id);
        }
        pantryItemRepository.deleteById(id);
    }
}
