import api from './api';
import type { ItemListaCompras, ItemListaComprasRequest } from '../types';

/**
 * Serviço para operações com a lista de compras.
 */
export const listaComprasService = {
  /**
   * Lista todos os itens da lista de compras.
   */
  async listarTodos(apenasNaoComprados: boolean = false): Promise<ItemListaCompras[]> {
    const response = await api.get<ItemListaCompras[]>('/shopping-list', {
      params: { apenasNaoComprados },
    });
    return response.data;
  },

  /**
   * Adiciona um novo item à lista de compras.
   */
  async adicionar(item: ItemListaComprasRequest): Promise<ItemListaCompras> {
    const response = await api.post<ItemListaCompras>('/shopping-list', item);
    return response.data;
  },

  /**
   * Remove um item da lista de compras.
   */
  async remover(id: string): Promise<void> {
    await api.delete(`/shopping-list/${id}`);
  },

  /**
   * Gera automaticamente a lista de compras baseada em produtos com estoque baixo.
   */
  async gerarAutomatica(): Promise<ItemListaCompras[]> {
    const response = await api.post<ItemListaCompras[]>('/shopping-list/auto-generate');
    return response.data;
  },
};
