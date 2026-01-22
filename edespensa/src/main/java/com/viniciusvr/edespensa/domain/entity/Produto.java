package com.viniciusvr.edespensa.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio que representa um produto na despensa.
 * Contém regras de negócio relacionadas ao produto.
 */
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProduto categoria;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    public Produto() {
        this.id = UUID.randomUUID().toString();
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.ativo = true;
    }

    public Produto(String nome, Integer quantidade, LocalDate dataValidade, CategoriaProduto categoria) {
        this();
        this.nome = nome;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.categoria = categoria;
    }

    /**
     * Consome uma quantidade do produto.
     * @param quantidadeConsumir quantidade a ser consumida
     * @throws IllegalArgumentException se a quantidade for inválida ou insuficiente
     */
    public void consumir(Integer quantidadeConsumir) {
        if (quantidadeConsumir == null || quantidadeConsumir <= 0) {
            throw new IllegalArgumentException("A quantidade a consumir deve ser maior que zero");
        }
        if (quantidadeConsumir > this.quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque. Disponível: " + this.quantidade);
        }
        this.quantidade -= quantidadeConsumir;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Repõe uma quantidade do produto.
     * @param quantidadeRepor quantidade a ser reposta
     * @throws IllegalArgumentException se a quantidade for inválida
     */
    public void repor(Integer quantidadeRepor) {
        if (quantidadeRepor == null || quantidadeRepor <= 0) {
            throw new IllegalArgumentException("A quantidade a repor deve ser maior que zero");
        }
        this.quantidade += quantidadeRepor;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Verifica se o produto está expirado.
     * @return true se o produto estiver expirado, false caso contrário
     */
    public boolean estaExpirado() {
        if (this.dataValidade == null) {
            return false;
        }
        return LocalDate.now().isAfter(this.dataValidade);
    }

    /**
     * Verifica se o produto está com estoque baixo.
     * @param limiar o limite para considerar estoque baixo
     * @return true se a quantidade for menor que o limiar
     */
    public boolean estaComEstoqueBaixo(Integer limiar) {
        return this.quantidade < limiar;
    }

    /**
     * Marca o produto como inativo (soft delete).
     */
    public void descartar() {
        this.ativo = false;
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

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProduto categoria) {
        this.categoria = categoria;
        this.atualizadoEm = LocalDateTime.now();
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
