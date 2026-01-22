package com.viniciusvr.edespensa.infrastructure.config;

import com.viniciusvr.edespensa.application.usecase.dashboard.DashboardUseCase;
import com.viniciusvr.edespensa.application.usecase.pantry.ConsumePantryItemUseCase;
import com.viniciusvr.edespensa.application.usecase.pantry.DiscardPantryItemUseCase;
import com.viniciusvr.edespensa.application.usecase.pantry.PantryManagementUseCase;
import com.viniciusvr.edespensa.application.usecase.product.ProductManagementUseCase;
import com.viniciusvr.edespensa.application.usecase.shoppinglist.ShoppingListManagementUseCase;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;
import com.viniciusvr.edespensa.domain.repository.ProductRepository;
import com.viniciusvr.edespensa.domain.repository.ShoppingListItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for use case beans.
 * This wires up the domain use cases with their infrastructure implementations.
 */
@Configuration
public class UseCaseConfig {

    @Bean
    public ProductManagementUseCase productManagementUseCase(ProductRepository productRepository) {
        return new ProductManagementUseCase(productRepository);
    }

    @Bean
    public PantryManagementUseCase pantryManagementUseCase(PantryItemRepository pantryItemRepository,
                                                           ProductRepository productRepository) {
        return new PantryManagementUseCase(pantryItemRepository, productRepository);
    }

    @Bean
    public ConsumePantryItemUseCase consumePantryItemUseCase(PantryItemRepository pantryItemRepository) {
        return new ConsumePantryItemUseCase(pantryItemRepository);
    }

    @Bean
    public DiscardPantryItemUseCase discardPantryItemUseCase(PantryItemRepository pantryItemRepository) {
        return new DiscardPantryItemUseCase(pantryItemRepository);
    }

    @Bean
    public ShoppingListManagementUseCase shoppingListManagementUseCase(
            ShoppingListItemRepository shoppingListItemRepository,
            ProductRepository productRepository) {
        return new ShoppingListManagementUseCase(shoppingListItemRepository, productRepository);
    }

    @Bean
    public DashboardUseCase dashboardUseCase(PantryItemRepository pantryItemRepository,
                                             ShoppingListItemRepository shoppingListItemRepository) {
        return new DashboardUseCase(pantryItemRepository, shoppingListItemRepository);
    }
}
