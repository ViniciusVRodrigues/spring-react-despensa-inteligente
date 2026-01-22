package com.viniciusvr.edespensa.application.usecase;

import com.viniciusvr.edespensa.application.gateway.ProdutoGateway;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.domain.exception.ProdutoNaoEncontradoException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para atualizar um produto existente.
 */
@Service
public class AtualizarProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public AtualizarProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    /**
     * Executa a atualização de um produto.
     * @param id o ID do produto a ser atualizado
     * @param produtoAtualizado os novos dados do produto
     * @return o produto atualizado
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado
     */
    public Produto executar(String id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoGateway.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        produtoExistente.setDataValidade(produtoAtualizado.getDataValidade());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());

        return produtoGateway.salvar(produtoExistente);
    }
}
