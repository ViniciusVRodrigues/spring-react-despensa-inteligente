package com.viniciusvr.edespensa.application.usecase.product;

import com.viniciusvr.edespensa.domain.entity.Product;
import com.viniciusvr.edespensa.domain.exception.BusinessRuleException;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.repository.ProductRepository;

import java.util.List;

/**
 * Use case for managing products (CRUD operations).
 */
public class ProductManagementUseCase {

    private final ProductRepository productRepository;

    public ProductManagementUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, String category, String unit, String description, boolean trackExpiration) {
        if (productRepository.existsByName(name)) {
            throw new BusinessRuleException("Product with name '" + name + "' already exists");
        }
        Product product = Product.createNew(name, category, unit, description, trackExpiration);
        return productRepository.save(product);
    }

    public Product createSimpleProduct(String name) {
        if (productRepository.existsByName(name)) {
            return productRepository.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Product", name));
        }
        Product product = Product.createNew(name, "Outros", "un", null, false);
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product", name));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String searchTerm) {
        return productRepository.findByNameContaining(searchTerm);
    }

    public Product updateProduct(Long id, String name, String category, String unit, String description, boolean trackExpiration) {
        Product product = getProductById(id);
        
        if (!product.getName().equals(name) && productRepository.existsByName(name)) {
            throw new BusinessRuleException("Product with name '" + name + "' already exists");
        }
        
        product.setName(name);
        product.setCategory(category);
        product.setUnit(unit);
        product.setDescription(description);
        product.setTrackExpiration(trackExpiration);
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }
}
