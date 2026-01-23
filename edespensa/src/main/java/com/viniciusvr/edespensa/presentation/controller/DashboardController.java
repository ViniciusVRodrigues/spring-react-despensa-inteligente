package com.viniciusvr.edespensa.presentation.controller;

import com.viniciusvr.edespensa.application.dto.DashboardAlertsDto;
import com.viniciusvr.edespensa.application.usecase.dashboard.DashboardUseCase;
import com.viniciusvr.edespensa.presentation.dto.request.AddAlertsToShoppingListRequest;
import com.viniciusvr.edespensa.presentation.dto.response.AddToShoppingListResponse;
import com.viniciusvr.edespensa.presentation.dto.response.DashboardAlertsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for dashboard operations.
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard and alerts endpoints")
public class DashboardController {

    private final DashboardUseCase dashboardUseCase;

    public DashboardController(DashboardUseCase dashboardUseCase) {
        this.dashboardUseCase = dashboardUseCase;
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get dashboard alerts", 
               description = "Returns alerts for products that are expiring soon, expired, or low on stock")
    public ResponseEntity<DashboardAlertsResponse> getAlerts() {
        DashboardAlertsDto alerts = dashboardUseCase.getAlerts();
        return ResponseEntity.ok(DashboardAlertsResponse.fromDto(alerts));
    }

    @PostMapping("/alerts/add-to-shopping-list")
    @Operation(summary = "Add all alerts to shopping list", 
               description = "Adds all expiring and low stock items to the shopping list")
    public ResponseEntity<AddToShoppingListResponse> addAllAlertsToShoppingList() {
        int itemsAdded = dashboardUseCase.addAlertsToShoppingList();
        return ResponseEntity.ok(AddToShoppingListResponse.success(itemsAdded));
    }

    @PostMapping("/alerts/add-selected-to-shopping-list")
    @Operation(summary = "Add selected alerts to shopping list", 
               description = "Adds selected pantry items from alerts to the shopping list")
    public ResponseEntity<AddToShoppingListResponse> addSelectedAlertsToShoppingList(
            @Valid @RequestBody AddAlertsToShoppingListRequest request) {
        int itemsAdded = dashboardUseCase.addSelectedAlertsToShoppingList(request.pantryItemIds());
        return ResponseEntity.ok(AddToShoppingListResponse.success(itemsAdded));
    }
}
