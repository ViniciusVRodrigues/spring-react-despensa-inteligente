package com.viniciusvr.edespensa.infrastructure.persistence.mapper;

import com.viniciusvr.edespensa.domain.entity.ShoppingListItem;
import com.viniciusvr.edespensa.infrastructure.persistence.entity.ShoppingListItemJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between ShoppingListItem domain entity and JPA entity.
 */
@Component
public class ShoppingListItemMapper {

    private final ProductMapper productMapper;

    public ShoppingListItemMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ShoppingListItem toDomain(ShoppingListItemJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new ShoppingListItem(
            jpaEntity.getId(),
            productMapper.toDomain(jpaEntity.getProduct()),
            jpaEntity.getQuantity(),
            mapPriorityToDomain(jpaEntity.getPriority()),
            mapStatusToDomain(jpaEntity.getStatus()),
            jpaEntity.getAddedAt(),
            jpaEntity.getNotes(),
            jpaEntity.isAutoAdded()
        );
    }

    public ShoppingListItemJpaEntity toJpaEntity(ShoppingListItem domain) {
        if (domain == null) return null;
        
        return new ShoppingListItemJpaEntity(
            domain.getId(),
            productMapper.toJpaEntity(domain.getProduct()),
            domain.getQuantity(),
            mapPriorityToJpa(domain.getPriority()),
            mapStatusToJpa(domain.getStatus()),
            domain.getAddedAt(),
            domain.getNotes(),
            domain.isAutoAdded()
        );
    }

    public void updateJpaEntity(ShoppingListItemJpaEntity jpaEntity, ShoppingListItem domain) {
        jpaEntity.setQuantity(domain.getQuantity());
        jpaEntity.setPriority(mapPriorityToJpa(domain.getPriority()));
        jpaEntity.setStatus(mapStatusToJpa(domain.getStatus()));
        jpaEntity.setNotes(domain.getNotes());
    }

    private ShoppingListItem.Priority mapPriorityToDomain(ShoppingListItemJpaEntity.Priority jpaPriority) {
        return ShoppingListItem.Priority.valueOf(jpaPriority.name());
    }

    private ShoppingListItemJpaEntity.Priority mapPriorityToJpa(ShoppingListItem.Priority domainPriority) {
        return ShoppingListItemJpaEntity.Priority.valueOf(domainPriority.name());
    }

    private ShoppingListItem.Status mapStatusToDomain(ShoppingListItemJpaEntity.Status jpaStatus) {
        return ShoppingListItem.Status.valueOf(jpaStatus.name());
    }

    private ShoppingListItemJpaEntity.Status mapStatusToJpa(ShoppingListItem.Status domainStatus) {
        return ShoppingListItemJpaEntity.Status.valueOf(domainStatus.name());
    }
}
