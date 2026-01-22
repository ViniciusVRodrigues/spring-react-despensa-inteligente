package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ProdutoGateway;
import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvr.edespensa.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ConsumirProdutoUseCase.
 */
@ExtendWith(MockitoExtension.class)
class ConsumirProdutoUseCaseTest {

    @Mock
    private ProdutoGateway produtoGateway;

    private ConsumirProdutoUseCase consumirProdutoUseCase;

    @BeforeEach
    void setUp() {
        consumirProdutoUseCase = new ConsumirProdutoUseCase(produtoGateway);
    }

    @Test
    void deveConsumirProdutoComSucesso() {
        Produto produto = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);
        String produtoId = produto.getId();

        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.of(produto));
        when(produtoGateway.salvar(any(Produto.class))).thenReturn(produto);

        Produto resultado = consumirProdutoUseCase.executar(produtoId, 5);

        assertNotNull(resultado);
        assertEquals(5, resultado.getQuantidade());
        verify(produtoGateway).salvar(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        String produtoId = "id-inexistente";
        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.empty());

        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> consumirProdutoUseCase.executar(produtoId, 5)
        );

        verify(produtoGateway, never()).salvar(any());
    }

    @Test
    void deveConsumirEmLoteComSucesso() {
        Produto produto1 = new Produto("Arroz", 10, null, CategoriaProduto.GRAOS);
        Produto produto2 = new Produto("Feijão", 8, null, CategoriaProduto.GRAOS);

        Map<String, Integer> consumos = Map.of(
                produto1.getId(), 3,
                produto2.getId(), 2
        );

        when(produtoGateway.buscarPorId(produto1.getId())).thenReturn(Optional.of(produto1));
        when(produtoGateway.buscarPorId(produto2.getId())).thenReturn(Optional.of(produto2));
        when(produtoGateway.salvar(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = consumirProdutoUseCase.executarEmLote(consumos);

        assertEquals(2, resultado.size());
        verify(produtoGateway, times(2)).salvar(any());
    }

    @Test
    void deveLancarExcecaoNoLoteQuandoQuantidadeInsuficiente() {
        Produto produto = new Produto("Feijão", 3, null, CategoriaProduto.GRAOS);

        Map<String, Integer> consumos = Map.of(
                produto.getId(), 10 // quantidade insuficiente
        );

        when(produtoGateway.buscarPorId(produto.getId())).thenReturn(Optional.of(produto));

        assertThrows(
                RegraNegocioException.class,
                () -> consumirProdutoUseCase.executarEmLote(consumos)
        );

        verify(produtoGateway, never()).salvar(any());
    }
}
