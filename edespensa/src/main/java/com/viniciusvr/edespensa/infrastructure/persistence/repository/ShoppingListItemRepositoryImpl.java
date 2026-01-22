package com.viniciusvr.edespensa.infrastructure.persistence.repository;

import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import com.viniciusvr.edespensa.domain.exception.EntityNotFoundException;
import com.viniciusvr.edespensa.domain.repository.ShoppingListItemRepository;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.ProductJpaEntity;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.ShoppingListItemJpaEntity;
import com.viniciusvr.edespensa.infrastructure.persistence.mapper.ShoppingListItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ShoppingListItemRepository using Spring Data JPA.
 */
@Repository
public class ShoppingListItemRepositoryImpl implements ShoppingListItemRepository {

    private final ShoppingListItemJpaRepository jpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ShoppingListItemMapper mapper;

    public ShoppingListItemRepositoryImpl(ShoppingListItemJpaRepository jpaRepository,
                                          ProductJpaRepository productJpaRepository,
                                          ShoppingListItemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ShoppingListItem save(ShoppingListItem item) {
        ShoppingListItemJpaEntity jpaEntity;
        
        if (item.getId() != null) {
            jpaEntity = jpaRepository.findById(item.getId())
                    .orElse(mapper.toJpaEntity(item));
            mapper.updateJpaEntity(jpaEntity, item);
        } else {
            jpaEntity = mapper.toJpaEntity(item);
            // Ensure the product is managed
            if (item.getProduct() != null && item.getProduct().getId() != null) {
                ProductJpaEntity productJpa = productJpaRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Product", item.getProduct().getId()));
                jpaEntity.setProduct(productJpa);
            }
        }
        
        ShoppingListItemJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ShoppingListItem> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ShoppingListItem> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingListItem> findByStatus(ShoppingListItem.Status status) {
        ShoppingListItemJpaEntity.Status jpaStatus = ShoppingListItemJpaEntity.Status.valueOf(status.name());
        return jpaRepository.findByStatus(jpaStatus).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingListItem> findPendingItems() {
        return jpaRepository.findPendingItems().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingListItem> findByProductId(Long productId) {
        return jpaRepository.findByProductId(productId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShoppingListItem> findPendingByProductId(Long productId) {
        return jpaRepository.findPendingByProductId(productId).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(ShoppingListItem item) {
        jpaRepository.deleteById(item.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsPendingByProductId(Long productId) {
        return jpaRepository.existsPendingByProductId(productId);
    }
}
