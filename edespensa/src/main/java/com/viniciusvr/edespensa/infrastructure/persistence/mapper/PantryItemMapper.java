package com.viniciusvr.edespensa.infrastructure.persistence.mapper;

import com.viniciusvr.edespensa.domain.entity.PantryItem;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.PantryItemJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between PantryItem domain entity and JPA entity.
 */
@Component
public class PantryItemMapper {

    private final ProductMapper productMapper;

    public PantryItemMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public PantryItem toDomain(PantryItemJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new PantryItem(
            jpaEntity.getId(),
            productMapper.toDomain(jpaEntity.getProduct()),
            jpaEntity.getQuantity(),
            jpaEntity.getExpirationDate(),
            jpaEntity.getAddedDate(),
            jpaEntity.getLocation(),
            jpaEntity.getNotes()
        );
    }

    public PantryItemJpaEntity toJpaEntity(PantryItem domain) {
        if (domain == null) return null;
        
        return new PantryItemJpaEntity(
            domain.getId(),
            productMapper.toJpaEntity(domain.getProduct()),
            domain.getQuantity(),
            domain.getExpirationDate(),
            domain.getAddedDate(),
            domain.getLocation(),
            domain.getNotes()
        );
    }

    public void updateJpaEntity(PantryItemJpaEntity jpaEntity, PantryItem domain) {
        jpaEntity.setQuantity(domain.getQuantity());
        jpaEntity.setExpirationDate(domain.getExpirationDate());
        jpaEntity.setLocation(domain.getLocation());
        jpaEntity.setNotes(domain.getNotes());
    }
}
