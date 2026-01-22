package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.gateway.ItemListaComprasGateway;
import com.viniciusvr.edespensa.domain.gateway.ProdutoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para GerarListaComprasAutomaticaUseCase.
 */
@ExtendWith(MockitoExtension.class)
class GerarListaComprasAutomaticaUseCaseTest {

    @Mock
    private ProdutoGateway produtoGateway;

    @Mock
    private ItemListaComprasGateway itemListaComprasGateway;

    private GerarListaComprasAutomaticaUseCase gerarListaComprasAutomaticaUseCase;

    @BeforeEach
    void setUp() {
        gerarListaComprasAutomaticaUseCase = new GerarListaComprasAutomaticaUseCase(
                produtoGateway,
                itemListaComprasGateway
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutos() {
        when(produtoGateway.listarTodosAtivos()).thenReturn(List.of());

        List<ItemListaCompras> resultado = gerarListaComprasAutomaticaUseCase.executar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveGerarListaParaProdutosComEstoqueBaixo() {
        // Produtos: média = (10 + 50 + 2) / 3 = 20.67
        // Limiar = 20% de 20.67 = 4.13 (arredondado para cima = 5)
        // Apenas produto com quantidade < 5 deve ser adicionado
        Produto produtoComEstoqueAlto = new Produto("Arroz", 50, null, CategoriaProduto.GRAOS);
        Produto produtoComEstoqueMedio = new Produto("Feijão", 10, null, CategoriaProduto.GRAOS);
        Produto produtoComEstoqueBaixo = new Produto("Sal", 2, null, CategoriaProduto.OUTROS);

        List<Produto> produtos = List.of(produtoComEstoqueAlto, produtoComEstoqueMedio, produtoComEstoqueBaixo);

        when(produtoGateway.listarTodosAtivos()).thenReturn(produtos);
        when(itemListaComprasGateway.salvarTodos(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<ItemListaCompras> resultado = gerarListaComprasAutomaticaUseCase.executar();

        // Apenas o Sal deve ser adicionado pois está abaixo do limiar
        assertEquals(1, resultado.size());
        assertEquals("Sal", resultado.get(0).getNome());
    }

    @Test
    void naoDeveAdicionarItensQuandoEstoqueAcimaDoLimiar() {
        // Com apenas 1 produto de quantidade 1:
        // - Média = 1
        // - Limiar = 20% de 1 = 0.2, arredondado para cima = 1
        // - Produto quantidade (1) NÃO é menor que limiar (1)
        // - Portanto, nenhum item deve ser adicionado à lista
        Produto produto = new Produto("Sal", 1, null, CategoriaProduto.OUTROS);
        List<Produto> produtos = List.of(produto);

        when(produtoGateway.listarTodosAtivos()).thenReturn(produtos);

        List<ItemListaCompras> resultado = gerarListaComprasAutomaticaUseCase.executar();

        assertTrue(resultado.isEmpty());
    }
}
