package com.viniciusvr.edespensa.presentation.controller;

import com.viniciusvr.edespensa.application.usecase.shoppinglist.ShoppingListManagementUseCase;
import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import com.viniciusvr.edespensa.presentation.dto.request.ShoppingListItemRequest;
import com.viniciusvr.edespensa.presentation.dto.request.ShoppingListItemUpdateRequest;
import com.viniciusvr.edespensa.presentation.dto.response.ShoppingListItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for shopping list management.
 */
@RestController
@RequestMapping("/api/shopping-list")
@Tag(name = "Shopping List", description = "Shopping list management endpoints")
public class ShoppingListController {

    private final ShoppingListManagementUseCase shoppingListUseCase;

    public ShoppingListController(ShoppingListManagementUseCase shoppingListUseCase) {
        this.shoppingListUseCase = shoppingListUseCase;
    }

    @PostMapping
    @Operation(summary = "Add item to shopping list", 
               description = "Adds an item to the shopping list. Creates product if using productName and it doesn't exist.")
    public ResponseEntity<ShoppingListItemResponse> addItem(@Valid @RequestBody ShoppingListItemRequest request) {
        if (!request.hasProductId() && !request.hasProductName()) {
            throw new IllegalArgumentException("Either productId or productName must be provided");
        }
        
        ShoppingListItem.Priority priority = request.priority() != null 
            ? ShoppingListItem.Priority.valueOf(request.priority().toUpperCase())
            : ShoppingListItem.Priority.MEDIUM;
        
        Double quantity = request.quantity() != null ? request.quantity() : 1.0;
        
        ShoppingListItem item;
        if (request.hasProductId()) {
            item = shoppingListUseCase.addItem(request.productId(), quantity, priority, request.notes());
        } else {
            item = shoppingListUseCase.addItemByName(request.productName(), quantity, priority, request.notes());
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListItemResponse.fromDomain(item));
    }

    @GetMapping
    @Operation(summary = "Get all shopping list items", description = "Returns all items in the shopping list")
    public ResponseEntity<List<ShoppingListItemResponse>> getAllItems() {
        List<ShoppingListItemResponse> items = shoppingListUseCase.getAllItems().stream()
                .map(ShoppingListItemResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending shopping list items", description = "Returns only pending (not purchased) items")
    public ResponseEntity<List<ShoppingListItemResponse>> getPendingItems() {
        List<ShoppingListItemResponse> items = shoppingListUseCase.getPendingItems().stream()
                .map(ShoppingListItemResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shopping list item by ID", description = "Returns a specific shopping list item")
    public ResponseEntity<ShoppingListItemResponse> getItemById(@PathVariable Long id) {
        ShoppingListItem item = shoppingListUseCase.getItemById(id);
        return ResponseEntity.ok(ShoppingListItemResponse.fromDomain(item));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shopping list item", description = "Updates an existing shopping list item")
    public ResponseEntity<ShoppingListItemResponse> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ShoppingListItemUpdateRequest request) {
        ShoppingListItem.Priority priority = request.priority() != null 
            ? ShoppingListItem.Priority.valueOf(request.priority().toUpperCase())
            : null;
        
        ShoppingListItem item = shoppingListUseCase.updateItem(id, request.quantity(), priority, request.notes());
        return ResponseEntity.ok(ShoppingListItemResponse.fromDomain(item));
    }

    @PostMapping("/{id}/purchased")
    @Operation(summary = "Mark item as purchased", description = "Marks a shopping list item as purchased")
    public ResponseEntity<Void> markAsPurchased(@PathVariable Long id) {
        shoppingListUseCase.markAsPurchased(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/purchased-batch")
    @Operation(summary = "Mark multiple items as purchased", description = "Marks multiple shopping list items as purchased")
    public ResponseEntity<Void> markAsPurchasedBatch(@RequestBody List<Long> ids) {
        shoppingListUseCase.markAsPurchasedBatch(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel shopping list item", description = "Cancels a shopping list item")
    public ResponseEntity<Void> cancelItem(@PathVariable Long id) {
        shoppingListUseCase.cancelItem(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shopping list item", description = "Deletes a shopping list item")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        shoppingListUseCase.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear-purchased")
    @Operation(summary = "Clear purchased items", description = "Removes all purchased items from the shopping list")
    public ResponseEntity<Void> clearPurchasedItems() {
        shoppingListUseCase.clearPurchasedItems();
        return ResponseEntity.noContent().build();
    }
}
