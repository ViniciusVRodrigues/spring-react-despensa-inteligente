package com.viniciusvr.edespensa.infrastructure.persistence.repository;

import com.viniciusvr.edespensa.infrastructure.persistence.entity.ShoppingListItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for ShoppingListItemJpaEntity.
 */
@Repository
public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItemJpaEntity, Long> {

    List<ShoppingListItemJpaEntity> findByStatus(ShoppingListItemJpaEntity.Status status);

    @Query("SELECT s FROM ShoppingListItemJpaEntity s WHERE s.status = 'PENDING'")
    List<ShoppingListItemJpaEntity> findPendingItems();

    List<ShoppingListItemJpaEntity> findByProductId(Long productId);

    @Query("SELECT s FROM ShoppingListItemJpaEntity s WHERE s.product.id = :productId AND s.status = 'PENDING'")
    Optional<ShoppingListItemJpaEntity> findPendingByProductId(@Param("productId") Long productId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShoppingListItemJpaEntity s WHERE s.product.id = :productId AND s.status = 'PENDING'")
    boolean existsPendingByProductId(@Param("productId") Long productId);
}
