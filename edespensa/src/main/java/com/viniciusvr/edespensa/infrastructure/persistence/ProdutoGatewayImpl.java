package com.viniciusvr.edespensa.infrastructure.persistence;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.gateway.ProdutoGateway;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do Gateway de Produto usando JPA.
 */
@Component
public class ProdutoGatewayImpl implements ProdutoGateway {

    private final ProdutoJpaRepository produtoJpaRepository;

    public ProdutoGatewayImpl(ProdutoJpaRepository produtoJpaRepository) {
        this.produtoJpaRepository = produtoJpaRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        return produtoJpaRepository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(String id) {
        return produtoJpaRepository.findById(id);
    }

    @Override
    public List<Produto> listarTodosAtivos() {
        return produtoJpaRepository.findByAtivoTrue();
    }

    @Override
    public List<Produto> listarPorCategoria(CategoriaProduto categoria) {
        return produtoJpaRepository.findByCategoriaAndAtivoTrue(categoria);
    }

    @Override
    public List<Produto> listarComEstoqueBaixo(Integer limiar) {
        return produtoJpaRepository.findByQuantidadeLessThanAndAtivoTrue(limiar);
    }

    @Override
    public List<Produto> listarExpirados() {
        return produtoJpaRepository.findByDataValidadeBeforeAndAtivoTrue(LocalDate.now());
    }

    @Override
    public void deletar(String id) {
        produtoJpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(String id) {
        return produtoJpaRepository.existsById(id);
    }

    @Override
    public List<Produto> salvarTodos(List<Produto> produtos) {
        return produtoJpaRepository.saveAll(produtos);
    }
}
