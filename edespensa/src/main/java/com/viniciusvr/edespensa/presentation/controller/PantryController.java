package com.viniciusvr.edespensa.presentation.controller;

import com.viniciusvr.edespensa.application.dto.BatchConsumptionDto;
import com.viniciusvr.edespensa.application.dto.ConsumptionItemDto;
import com.viniciusvr.edespensa.application.dto.ConsumptionResultDto;
import com.viniciusvr.edespensa.application.dto.QuickPurchaseDto;
import com.viniciusvr.edespensa.application.usecase.pantry.ConsumePantryItemUseCase;
import com.viniciusvr.edespensa.application.usecase.pantry.DiscardPantryItemUseCase;
import com.viniciusvr.edespensa.application.usecase.pantry.PantryManagementUseCase;
import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.presentation.dto.request.*;
import com.viniciusvr.edespensa.presentation.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for pantry management.
 */
@RestController
@RequestMapping("/api/pantry")
@Tag(name = "Pantry", description = "Pantry item management endpoints")
public class PantryController {

    private final PantryManagementUseCase pantryUseCase;
    private final ConsumePantryItemUseCase consumeUseCase;
    private final DiscardPantryItemUseCase discardUseCase;

    public PantryController(PantryManagementUseCase pantryUseCase,
                           ConsumePantryItemUseCase consumeUseCase,
                           DiscardPantryItemUseCase discardUseCase) {
        this.pantryUseCase = pantryUseCase;
        this.consumeUseCase = consumeUseCase;
        this.discardUseCase = discardUseCase;
    }

    @PostMapping
    @Operation(summary = "Add item to pantry", description = "Adds a new item to the pantry")
    public ResponseEntity<PantryItemResponse> addToPantry(@Valid @RequestBody PantryItemRequest request) {
        PantryItem item = pantryUseCase.addToPantry(
            request.productId(),
            request.quantity(),
            request.expirationDate(),
            request.location(),
            request.notes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(PantryItemResponse.fromDomain(item));
    }

    @PostMapping("/quick-purchase")
    @Operation(summary = "Quick purchase", 
               description = "Quick action to add purchased items to pantry. Creates product if it doesn't exist.")
    public ResponseEntity<PantryItemResponse> quickPurchase(@Valid @RequestBody QuickPurchaseRequest request) {
        if (!request.hasProductId() && !request.hasProductName()) {
            throw new IllegalArgumentException("Either productId or productName must be provided");
        }
        
        QuickPurchaseDto dto = new QuickPurchaseDto(
            request.productId(),
            request.productName(),
            request.category(),
            request.unit(),
            request.quantity(),
            request.expirationDate(),
            request.location(),
            request.notes()
        );
        
        PantryItem item = pantryUseCase.quickPurchase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(PantryItemResponse.fromDomain(item));
    }

    @GetMapping
    @Operation(summary = "Get all pantry items", description = "Returns all items in the pantry")
    public ResponseEntity<List<PantryItemResponse>> getAllPantryItems() {
        List<PantryItemResponse> items = pantryUseCase.getAllPantryItems().stream()
                .map(PantryItemResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pantry item by ID", description = "Returns a specific pantry item")
    public ResponseEntity<PantryItemResponse> getPantryItemById(@PathVariable Long id) {
        PantryItem item = pantryUseCase.getPantryItemById(id);
        return ResponseEntity.ok(PantryItemResponse.fromDomain(item));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get pantry items by product", description = "Returns all pantry items for a specific product")
    public ResponseEntity<List<PantryItemResponse>> getPantryItemsByProduct(@PathVariable Long productId) {
        List<PantryItemResponse> items = pantryUseCase.getPantryItemsByProduct(productId).stream()
                .map(PantryItemResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    @Operation(summary = "Search pantry items", description = "Search pantry items by product name")
    public ResponseEntity<List<PantryItemResponse>> searchPantryItems(@RequestParam String q) {
        List<PantryItemResponse> items = pantryUseCase.searchPantryItemsByProductName(q).stream()
                .map(PantryItemResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pantry item", description = "Updates an existing pantry item")
    public ResponseEntity<PantryItemResponse> updatePantryItem(
            @PathVariable Long id,
            @Valid @RequestBody PantryItemUpdateRequest request) {
        PantryItem item = pantryUseCase.updatePantryItem(
            id,
            request.quantity(),
            request.expirationDate(),
            request.location(),
            request.notes()
        );
        return ResponseEntity.ok(PantryItemResponse.fromDomain(item));
    }

    @PostMapping("/{id}/consume")
    @Operation(summary = "Consume pantry item", 
               description = "Consumes a quantity from a pantry item. Returns info about depleted items.")
    public ResponseEntity<ConsumptionResponse> consumeItem(
            @PathVariable Long id,
            @Valid @RequestBody ConsumeRequest request) {
        ConsumptionResultDto result = consumeUseCase.consumeItem(id, request.quantity());
        return ResponseEntity.ok(ConsumptionResponse.from(
            result.itemsConsumed(),
            result.depletedItemIds(),
            result.depletedProductNames(),
            result.message()
        ));
    }

    @PostMapping("/consume-batch")
    @Operation(summary = "Batch consume pantry items", 
               description = "Consumes multiple pantry items at once. Supports batch operations.")
    public ResponseEntity<ConsumptionResponse> consumeBatch(@Valid @RequestBody BatchConsumeRequest request) {
        List<ConsumptionItemDto> items = request.items().stream()
                .map(i -> new ConsumptionItemDto(i.pantryItemId(), i.quantity()))
                .collect(Collectors.toList());
        
        BatchConsumptionDto batchDto = new BatchConsumptionDto(items);
        ConsumptionResultDto result = consumeUseCase.consumeBatch(batchDto);
        
        return ResponseEntity.ok(ConsumptionResponse.from(
            result.itemsConsumed(),
            result.depletedItemIds(),
            result.depletedProductNames(),
            result.message()
        ));
    }

    @PostMapping("/{id}/discard")
    @Operation(summary = "Discard pantry item", description = "Discards a pantry item (e.g., expired or damaged)")
    public ResponseEntity<DiscardResponse> discardItem(@PathVariable Long id) {
        String productName = discardUseCase.discardItem(id);
        return ResponseEntity.ok(DiscardResponse.single(productName));
    }

    @PostMapping("/discard-batch")
    @Operation(summary = "Batch discard pantry items", description = "Discards multiple pantry items at once")
    public ResponseEntity<DiscardResponse> discardBatch(@RequestBody List<Long> pantryItemIds) {
        List<String> discardedNames = discardUseCase.discardItems(pantryItemIds);
        return ResponseEntity.ok(DiscardResponse.multiple(discardedNames));
    }

    @PostMapping("/discard-expired")
    @Operation(summary = "Discard all expired items", description = "Discards all expired items from the pantry")
    public ResponseEntity<DiscardResponse> discardAllExpired() {
        List<String> discardedNames = discardUseCase.discardAllExpired();
        return ResponseEntity.ok(DiscardResponse.multiple(discardedNames));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pantry item", description = "Deletes a pantry item")
    public ResponseEntity<Void> deletePantryItem(@PathVariable Long id) {
        pantryUseCase.deletePantryItem(id);
        return ResponseEntity.noContent().build();
    }
}
