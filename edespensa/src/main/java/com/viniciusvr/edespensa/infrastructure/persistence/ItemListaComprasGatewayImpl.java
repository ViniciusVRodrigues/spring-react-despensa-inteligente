package com.viniciusvr.edespensa.infrastructure.persistence;

import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
import com.viniciusvr.edespensa.domain.gateway.ItemListaComprasGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do Gateway de ItemListaCompras usando JPA.
 */
@Component
public class ItemListaComprasGatewayImpl implements ItemListaComprasGateway {

    private final ItemListaComprasJpaRepository itemListaComprasJpaRepository;

    public ItemListaComprasGatewayImpl(ItemListaComprasJpaRepository itemListaComprasJpaRepository) {
        this.itemListaComprasJpaRepository = itemListaComprasJpaRepository;
    }

    @Override
    public ItemListaCompras salvar(ItemListaCompras item) {
        return itemListaComprasJpaRepository.save(item);
    }

    @Override
    public Optional<ItemListaCompras> buscarPorId(String id) {
        return itemListaComprasJpaRepository.findById(id);
    }

    @Override
    public List<ItemListaCompras> listarTodos() {
        return itemListaComprasJpaRepository.findAll();
    }

    @Override
    public List<ItemListaCompras> listarNaoComprados() {
        return itemListaComprasJpaRepository.findByCompradoFalse();
    }

    @Override
    public void deletar(String id) {
        itemListaComprasJpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(String id) {
        return itemListaComprasJpaRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deletarComprados() {
        itemListaComprasJpaRepository.deleteByCompradoTrue();
    }

    @Override
    public List<ItemListaCompras> salvarTodos(List<ItemListaCompras> itens) {
        return itemListaComprasJpaRepository.saveAll(itens);
    }
}
