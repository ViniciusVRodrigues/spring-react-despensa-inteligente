package com.viniciusvr.edespensa.domain.repository;

import com.viniciusvr.edespensa.domain.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface (port) for Product entity.
 * This is a domain interface that will be implemented by infrastructure layer.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    List<Product> findByCategory(String category);

    List<Product> findByNameContaining(String name);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}
