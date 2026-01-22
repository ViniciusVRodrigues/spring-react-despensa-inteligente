package com.viniciusvr.edespensa.application.usecase.dashboard;

import com.viniciusvr.edespensa.application.dto.AlertItemDto;
import com.viniciusvr.edespensa.application.dto.DashboardAlertsDto;
import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;
import com.viniciusvr.edespensa.domain.repository.ShoppingListItemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for dashboard alerts and quick actions.
 */
public class DashboardUseCase {

    private final int expiringSoonDays;
    private final double lowStockThreshold;

    private final PantryItemRepository pantryItemRepository;
    private final ShoppingListItemRepository shoppingListRepository;

    public DashboardUseCase(PantryItemRepository pantryItemRepository, 
                           ShoppingListItemRepository shoppingListRepository,
                           int expiringSoonDays,
                           double lowStockThreshold) {
        this.pantryItemRepository = pantryItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.expiringSoonDays = expiringSoonDays;
        this.lowStockThreshold = lowStockThreshold;
    }

    /**
     * Get all dashboard alerts for products that need attention.
     */
    public DashboardAlertsDto getAlerts() {
        List<PantryItem> allItems = pantryItemRepository.findAll();
        
        List<AlertItemDto> expiringSoon = new ArrayList<>();
        List<AlertItemDto> expired = new ArrayList<>();
        List<AlertItemDto> lowStock = new ArrayList<>();
        
        for (PantryItem item : allItems) {
            if (item.isExpired()) {
                expired.add(createAlertItem(item, "EXPIRED", "Product has expired"));
            } else if (item.isExpiringSoon(expiringSoonDays)) {
                expiringSoon.add(createAlertItem(item, "EXPIRING_SOON", 
                    "Expires in " + calculateDaysUntilExpiration(item) + " days"));
            }
            
            if (item.isLowStock(lowStockThreshold)) {
                lowStock.add(createAlertItem(item, "LOW_STOCK", "Low stock - only " + item.getQuantity() + " left"));
            }
        }
        
        return DashboardAlertsDto.of(expiringSoon, expired, lowStock);
    }

    /**
     * Add all expiring and low stock items to shopping list.
     */
    public int addAlertsToShoppingList() {
        DashboardAlertsDto alerts = getAlerts();
        int addedCount = 0;
        
        // Add expiring soon items
        for (AlertItemDto alert : alerts.expiringSoon()) {
            if (!shoppingListRepository.existsPendingByProductId(alert.productId())) {
                ShoppingListItem item = ShoppingListItem.createAutoAdded(
                    createProductFromAlert(alert), 
                    1.0, 
                    "Added automatically - expiring soon"
                );
                shoppingListRepository.save(item);
                addedCount++;
            }
        }
        
        // Add low stock items
        for (AlertItemDto alert : alerts.lowStock()) {
            if (!shoppingListRepository.existsPendingByProductId(alert.productId())) {
                ShoppingListItem item = ShoppingListItem.createAutoAdded(
                    createProductFromAlert(alert), 
                    1.0, 
                    "Added automatically - low stock"
                );
                shoppingListRepository.save(item);
                addedCount++;
            }
        }
        
        return addedCount;
    }

    /**
     * Add specific alert items to shopping list.
     */
    public int addSelectedAlertsToShoppingList(List<Long> pantryItemIds) {
        int addedCount = 0;
        
        for (Long pantryItemId : pantryItemIds) {
            PantryItem item = pantryItemRepository.findById(pantryItemId).orElse(null);
            if (item != null && !shoppingListRepository.existsPendingByProductId(item.getProduct().getId())) {
                ShoppingListItem shoppingItem = ShoppingListItem.createAutoAdded(
                    item.getProduct(), 
                    1.0, 
                    "Added from dashboard alerts"
                );
                shoppingListRepository.save(shoppingItem);
                addedCount++;
            }
        }
        
        return addedCount;
    }

    private AlertItemDto createAlertItem(PantryItem item, String alertType, String message) {
        return new AlertItemDto(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getProduct().getUnit(),
            item.getExpirationDate(),
            alertType,
            message
        );
    }

    private long calculateDaysUntilExpiration(PantryItem item) {
        if (item.getExpirationDate() == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), item.getExpirationDate());
    }

    private com.viniciusvr.edespensa.domain.entity.Product createProductFromAlert(AlertItemDto alert) {
        com.viniciusvr.edespensa.domain.entity.Product product = new com.viniciusvr.edespensa.domain.entity.Product();
        product.setId(alert.productId());
        product.setName(alert.productName());
        product.setUnit(alert.unit());
        return product;
    }
}
