package com.viniciusvr.edespensa.infrastructure.persistence;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para ItemListaCompras.
 */
@Repository
public interface ItemListaComprasJpaRepository extends JpaRepository<ItemListaCompras, String> {

    /**
     * Lista itens não comprados.
     */
    List<ItemListaCompras> findByCompradoFalse();

    /**
     * Lista itens comprados.
     */
    List<ItemListaCompras> findByCompradoTrue();

    /**
     * Deleta todos os itens comprados.
     */
    void deleteByCompradoTrue();
}
