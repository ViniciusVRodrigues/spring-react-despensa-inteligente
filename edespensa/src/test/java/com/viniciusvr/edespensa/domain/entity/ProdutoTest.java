package com.viniciusvr.edespensa.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Produto.
 */
class ProdutoTest {

    @Test
    void deveCriarProdutoComSucesso() {
        Produto produto = new Produto("Arroz", 10, LocalDate.now().plusDays(30), CategoriaProduto.GRAOS);

        assertNotNull(produto.getId());
        assertEquals("Arroz", produto.getNome());
        assertEquals(10, produto.getQuantidade());
        assertEquals(CategoriaProduto.GRAOS, produto.getCategoria());
        assertTrue(produto.getAtivo());
    }

    @Test
    void deveConsumirProdutoQuandoQuantidadeDisponivel() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);

        produto.consumir(5);

        assertEquals(5, produto.getQuantidade());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeConsumoMaiorQueDisponivel() {
        Produto produto = new Produto("Arroz", 5, null, CategoriaProduto.GRAOS);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produto.consumir(10)
        );

        assertTrue(exception.getMessage().contains("Quantidade insuficiente"));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeConsumoZeroOuNegativa() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);

        assertThrows(IllegalArgumentException.class, () -> produto.consumir(0));
        assertThrows(IllegalArgumentException.class, () -> produto.consumir(-1));
    }

    @Test
    void deveReporProdutoComSucesso() {
        Produto produto = new Produto("Arroz", 5, null, CategoriaProduto.GRAOS);

        produto.repor(10);

        assertEquals(15, produto.getQuantidade());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeReposicaoZeroOuNegativa() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);

        assertThrows(IllegalArgumentException.class, () -> produto.repor(0));
        assertThrows(IllegalArgumentException.class, () -> produto.repor(-1));
    }

    @Test
    void deveRetornarTrueQuandoProdutoExpirado() {
        Produto produto = new Produto("Leite", 5, LocalDate.now().minusDays(1), CategoriaProduto.LATICINIOS);

        assertTrue(produto.estaExpirado());
    }

    @Test
    void deveRetornarFalseQuandoProdutoNaoExpirado() {
        Produto produto = new Produto("Leite", 5, LocalDate.now().plusDays(10), CategoriaProduto.LATICINIOS);

        assertFalse(produto.estaExpirado());
    }

    @Test
    void deveRetornarFalseQuandoProdutoSemDataValidade() {
        Produto produto = new Produto("Sal", 5, null, CategoriaProduto.OUTROS);

        assertFalse(produto.estaExpirado());
    }

    @Test
    void deveRetornarTrueQuandoEstoqueBaixo() {
        Produto produto = new Produto("Arroz", 3, null, CategoriaProduto.GRAOS);

        assertTrue(produto.estaComEstoqueBaixo(5));
    }

    @Test
    void deveRetornarFalseQuandoEstoqueNaoEBaixo() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);

        assertFalse(produto.estaComEstoqueBaixo(5));
    }

    @Test
    void deveDescartarProdutoComSucesso() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);

        produto.descartar();

        assertFalse(produto.getAtivo());
    }
}
