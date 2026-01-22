import React, { useState, useEffect, useCallback } from 'react';
import toast from 'react-hot-toast';
import type { ItemListaCompras, ItemListaComprasRequest } from '../../../types';
import { listaComprasService } from '../../../services/listaComprasService';
import { ShoppingList } from '../../organisms/ShoppingList';
import styles from './ListaComprasPage.module.css';

/**
 * Página da Lista de Compras.
 */
export const ListaComprasPage: React.FC = () => {
  const [itens, setItens] = useState<ItemListaCompras[]>([]);
  const [loading, setLoading] = useState(true);

  const carregarItens = useCallback(async () => {
    try {
      setLoading(true);
      const dados = await listaComprasService.listarTodos();
      setItens(dados);
    } catch (error) {
      toast.error('Erro ao carregar lista de compras');
      console.error('Erro ao carregar lista de compras:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregarItens();
  }, [carregarItens]);

  const handleAdicionarItem = async (item: ItemListaComprasRequest) => {
    try {
      await listaComprasService.adicionar(item);
      toast.success('Item adicionado à lista!');
      await carregarItens();
    } catch (error) {
      toast.error('Erro ao adicionar item');
      console.error('Erro ao adicionar item:', error);
    }
  };

  const handleRemoverItem = async (id: string) => {
    try {
      await listaComprasService.remover(id);
      toast.success('Item removido da lista!');
      await carregarItens();
    } catch (error) {
      toast.error('Erro ao remover item');
      console.error('Erro ao remover item:', error);
    }
  };

  const handleGerarAutomatica = async () => {
    try {
      const novosItens = await listaComprasService.gerarAutomatica();
      if (novosItens.length === 0) {
        toast('Nenhum produto com estoque baixo encontrado', { icon: 'ℹ️' });
      } else {
        toast.success(`${novosItens.length} item(ns) adicionado(s) à lista!`);
      }
      await carregarItens();
    } catch (error) {
      toast.error('Erro ao gerar lista automática');
      console.error('Erro ao gerar lista automática:', error);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Lista de Compras</h2>
        <span className={styles.count}>{itens.length} item(ns)</span>
      </div>

      <ShoppingList
        itens={itens}
        loading={loading}
        onAdicionarItem={handleAdicionarItem}
        onRemoverItem={handleRemoverItem}
        onGerarAutomatica={handleGerarAutomatica}
      />
    </div>
  );
};
