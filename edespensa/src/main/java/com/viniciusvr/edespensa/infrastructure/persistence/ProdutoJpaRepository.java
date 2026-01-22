package com.viniciusvr.edespensa.infrastructure.persistence;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório JPA para Produto.
 */
@Repository
public interface ProdutoJpaRepository extends JpaRepository<Produto, String> {

    /**
     * Lista todos os produtos ativos.
     */
    List<Produto> findByAtivoTrue();

    /**
     * Lista produtos por categoria que estão ativos.
     */
    List<Produto> findByCategoriaAndAtivoTrue(CategoriaProduto categoria);

    /**
     * Lista produtos com quantidade menor que o limiar e ativos.
     */
    @Query("SELECT p FROM Produto p WHERE p.quantidade < :limiar AND p.ativo = true")
    List<Produto> findByQuantidadeLessThanAndAtivoTrue(@Param("limiar") Integer limiar);

    /**
     * Lista produtos com data de validade anterior à data especificada e ativos.
     */
    @Query("SELECT p FROM Produto p WHERE p.dataValidade < :data AND p.ativo = true")
    List<Produto> findByDataValidadeBeforeAndAtivoTrue(@Param("data") LocalDate data);
}
