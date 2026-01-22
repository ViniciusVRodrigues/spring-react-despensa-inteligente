package com.viniciusvr.edespensa.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio que representa um item na lista de compras.
 */
@Entity
@Table(name = "itens_lista_compras")
public class ItemListaCompras {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private CategoriaProduto categoria;

    @Column(nullable = false)
    private Boolean comprado;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public ItemListaCompras() {
        this.id = UUID.randomUUID().toString();
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.comprado = false;
    }

    public ItemListaCompras(String nome, Integer quantidade, CategoriaProduto categoria) {
        this();
        this.nome = nome;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    /**
     * Marca o item como comprado.
     */
    public void marcarComoComprado() {
        this.comprado = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Desmarca o item como comprado.
     */
    public void desmarcarComoComprado() {
        this.comprado = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProduto categoria) {
        this.categoria = categoria;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Boolean getComprado() {
        return comprado;
    }

    public void setComprado(Boolean comprado) {
        this.comprado = comprado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
