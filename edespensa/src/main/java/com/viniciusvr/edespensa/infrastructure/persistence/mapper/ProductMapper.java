package com.viniciusvr.edespensa.infrastructure.persistence.mapper;

import com.viniciusvr.edespensa.domain.entity.Product;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Product domain entity and JPA entity.
 */
@Component
public class ProductMapper {

    public Product toDomain(ProductJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new Product(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getCategory(),
            jpaEntity.getUnit(),
            jpaEntity.getDescription(),
            jpaEntity.isTrackExpiration()
        );
    }

    public ProductJpaEntity toJpaEntity(Product domain) {
        if (domain == null) return null;
        
        return new ProductJpaEntity(
            domain.getId(),
            domain.getName(),
            domain.getCategory(),
            domain.getUnit(),
            domain.getDescription(),
            domain.isTrackExpiration()
        );
    }

    public void updateJpaEntity(ProductJpaEntity jpaEntity, Product domain) {
        jpaEntity.setName(domain.getName());
        jpaEntity.setCategory(domain.getCategory());
        jpaEntity.setUnit(domain.getUnit());
        jpaEntity.setDescription(domain.getDescription());
        jpaEntity.setTrackExpiration(domain.isTrackExpiration());
    }
}
