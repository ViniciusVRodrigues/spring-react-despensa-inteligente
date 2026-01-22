package com.viniciusvr.edespensa.domain.entity;

/**
 * Enum que representa as categorias de produtos disponíveis na despensa.
 */
public enum CategoriaProduto {
    GRAOS("Grãos"),
    LATICINIOS("Laticínios"),
    BEBIDAS("Bebidas"),
    ENLATADOS("Enlatados"),
    CONGELADOS("Congelados"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
