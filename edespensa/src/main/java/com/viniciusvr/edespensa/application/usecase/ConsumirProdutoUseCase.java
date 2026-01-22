package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ProdutoGateway;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvr.edespensa.domain.exception.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Caso de uso para consumir produtos da despensa.
 */
@Service
public class ConsumirProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public ConsumirProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Executa o consumo de um produto.
     * @param id o ID do produto
     * @param quantidade a quantidade a ser consumida
     * @return o produto atualizado
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     * @throws IllegalArgumentException se a quantidade for inválida
     */
    public Produto executar(String id, Integer quantidade) {
        Produto produto = produtoGateway.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.consumir(quantidade);
        return produtoGateway.salvar(produto);
    }

    /**
     * Executa o consumo em lote de produtos.
     * Valida disponibilidade de todos os produtos antes de processar.
     * @param consumos mapa de ID do produto para quantidade a consumir
     * @return lista de produtos atualizados
     * @throws RegraNegocioException se algum produto não tiver quantidade suficiente
     */
    public List<Produto> executarEmLote(Map<String, Integer> consumos) {
        // Primeiro, valida todos os produtos
        for (Map.Entry<String, Integer> entry : consumos.entrySet()) {
            String id = entry.getKey();
            Integer quantidade = entry.getValue();

            Produto produto = produtoGateway.buscarPorId(id)
                    .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

            if (produto.getQuantidade() < quantidade) {
                throw new RegraNegocioException(
                        "Produto '" + produto.getNome() + "' não possui quantidade suficiente. " +
                        "Disponível: " + produto.getQuantidade() + ", Solicitado: " + quantidade
                );
            }
        }

        // Se todos passaram na validação, executa o consumo
        return consumos.entrySet().stream()
                .map(entry -> {
                    Produto produto = produtoGateway.buscarPorId(entry.getKey()).get();
                    produto.consumir(entry.getValue());
                    return produtoGateway.salvar(produto);
                })
                .toList();
    }
}
