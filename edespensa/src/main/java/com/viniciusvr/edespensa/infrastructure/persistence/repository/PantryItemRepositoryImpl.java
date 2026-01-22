package com.viniciusvr.edespensa.infrastructure.persistence.repository;

import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.domain.repository.PantryItemRepository;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.PantryItemJpaEntity;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.ProductJpaEntity;
import com.viniciusvr.edespensa.infrastructure.persistence.mapper.PantryItemMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PantryItemRepository using Spring Data JPA.
 */
@Repository
public class PantryItemRepositoryImpl implements PantryItemRepository {

    private final PantryItemJpaRepository jpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final PantryItemMapper mapper;

    public PantryItemRepositoryImpl(PantryItemJpaRepository jpaRepository, 
                                    ProductJpaRepository productJpaRepository,
                                    PantryItemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PantryItem save(PantryItem pantryItem) {
        PantryItemJpaEntity jpaEntity;
        
        if (pantryItem.getId() != null) {
            jpaEntity = jpaRepository.findById(pantryItem.getId())
                    .orElse(mapper.toJpaEntity(pantryItem));
            mapper.updateJpaEntity(jpaEntity, pantryItem);
        } else {
            jpaEntity = mapper.toJpaEntity(pantryItem);
            // Ensure the product is managed
            if (pantryItem.getProduct() != null && pantryItem.getProduct().getId() != null) {
                ProductJpaEntity productJpa = productJpaRepository.findById(pantryItem.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                jpaEntity.setProduct(productJpa);
            }
        }
        
        PantryItemJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PantryItem> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PantryItem> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findByProductId(Long productId) {
        return jpaRepository.findByProductId(productId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findByProductName(String productName) {
        return jpaRepository.findByProductNameContaining(productName).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findByExpirationDateBefore(LocalDate date) {
        return jpaRepository.findByExpirationDateBefore(date).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByExpirationDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findByQuantityLessThanEqual(Double quantity) {
        return jpaRepository.findByQuantityLessThanEqual(quantity).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findExpiringSoon(int days) {
        LocalDate threshold = LocalDate.now().plusDays(days);
        return jpaRepository.findExpiringSoon(threshold).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PantryItem> findLowStock(Double threshold) {
        return jpaRepository.findLowStock(threshold).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(PantryItem pantryItem) {
        jpaRepository.deleteById(pantryItem.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
