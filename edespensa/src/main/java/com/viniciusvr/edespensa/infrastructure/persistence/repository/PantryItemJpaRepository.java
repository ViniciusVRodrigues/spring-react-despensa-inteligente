package com.viniciusvr.edespensa.infrastructure.persistence.repository;

import com.viniciusvr.edespensa.infrastructure.persistence.entity.PantryItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for PantryItemJpaEntity.
 */
@Repository
public interface PantryItemJpaRepository extends JpaRepository<PantryItemJpaEntity, Long> {

    List<PantryItemJpaEntity> findByProductId(Long productId);

    @Query("SELECT p FROM PantryItemJpaEntity p WHERE LOWER(p.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
    List<PantryItemJpaEntity> findByProductNameContaining(@Param("productName") String productName);

    List<PantryItemJpaEntity> findByExpirationDateBefore(LocalDate date);

    List<PantryItemJpaEntity> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    List<PantryItemJpaEntity> findByQuantityLessThanEqual(Double quantity);

    @Query("SELECT p FROM PantryItemJpaEntity p WHERE p.expirationDate IS NOT NULL AND p.expirationDate > CURRENT_DATE AND p.expirationDate <= :threshold")
    List<PantryItemJpaEntity> findExpiringSoon(@Param("threshold") LocalDate threshold);

    @Query("SELECT p FROM PantryItemJpaEntity p WHERE p.quantity <= :threshold")
    List<PantryItemJpaEntity> findLowStock(@Param("threshold") Double threshold);
}
