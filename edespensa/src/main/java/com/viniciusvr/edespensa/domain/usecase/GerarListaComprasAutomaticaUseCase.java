package com.viniciusvr.edespensa.domain.usecase;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.gateway.ItemListaComprasGateway;
import com.viniciusvr.edespensa.domain.gateway.ProdutoGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

/**
 * Caso de uso para gerar automaticamente a lista de compras
 * baseada em produtos com estoque baixo.
 * 
 * Regra de negócio: Produtos com quantidade < 20% do estoque médio
 * são adicionados automaticamente à lista de compras.
 */
@Service
public class GerarListaComprasAutomaticaUseCase {

    private final ProdutoGateway produtoGateway;
    private final ItemListaComprasGateway itemListaComprasGateway;

    public GerarListaComprasAutomaticaUseCase(
            ProdutoGateway produtoGateway,
            ItemListaComprasGateway itemListaComprasGateway) {
        this.produtoGateway = produtoGateway;
        this.itemListaComprasGateway = itemListaComprasGateway;
    }

    /**
     * Gera automaticamente a lista de compras baseada em produtos com estoque baixo.
     * @return lista de itens adicionados à lista de compras
     */
    public List<ItemListaCompras> executar() {
        List<Produto> produtosAtivos = produtoGateway.listarTodosAtivos();

        if (produtosAtivos.isEmpty()) {
            return List.of();
        }

        // Calcula o estoque médio
        OptionalDouble mediaOptional = produtosAtivos.stream()
                .mapToInt(Produto::getQuantidade)
                .average();

        if (mediaOptional.isEmpty()) {
            return List.of();
        }

        double media = mediaOptional.getAsDouble();
        int limiar = (int) Math.ceil(media * 0.2); // 20% do estoque médio

        // Filtra produtos com estoque baixo e cria itens de lista de compras
        List<ItemListaCompras> itensParaAdicionar = produtosAtivos.stream()
                .filter(produto -> produto.getQuantidade() < limiar)
                .map(produto -> {
                    // Quantidade sugerida: diferença para atingir a média
                    int quantidadeSugerida = (int) Math.ceil(media) - produto.getQuantidade();
                    if (quantidadeSugerida <= 0) {
                        quantidadeSugerida = 1;
                    }
                    return new ItemListaCompras(
                            produto.getNome(),
                            quantidadeSugerida,
                            produto.getCategoria()
                    );
                })
                .toList();

        return itemListaComprasGateway.salvarTodos(itensParaAdicionar);
    }
}
