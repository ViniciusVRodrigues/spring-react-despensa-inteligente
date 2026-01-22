import api from './api';
import type { Produto, ProdutoRequest, ConsumoLoteRequest } from '../types';
import { CategoriaProduto } from '../types';

/**
 * Serviço para operações com produtos da despensa.
 */
export const produtoService = {
  /**
   * Lista todos os produtos ativos da despensa.
   */
  async listarTodos(categoria?: CategoriaProduto): Promise<Produto[]> {
    const params = categoria ? { categoria } : {};
    const response = await api.get<Produto[]>('/products', { params });
    return response.data;
  },

  /**
   * Busca um produto específico pelo ID.
   */
  async buscarPorId(id: string): Promise<Produto> {
    const response = await api.get<Produto>(`/products/${id}`);
    return response.data;
  },

  /**
   * Cria um novo produto na despensa.
   */
  async criar(produto: ProdutoRequest): Promise<Produto> {
    const response = await api.post<Produto>('/products', produto);
    return response.data;
  },

  /**
   * Atualiza os dados de um produto existente.
   */
  async atualizar(id: string, produto: ProdutoRequest): Promise<Produto> {
    const response = await api.put<Produto>(`/products/${id}`, produto);
    return response.data;
  },

  /**
   * Descarta um produto (soft delete).
   */
  async descartar(id: string): Promise<void> {
    await api.delete(`/products/${id}`);
  },

  /**
   * Registra o consumo de produtos em lote.
   */
  async consumirEmLote(request: ConsumoLoteRequest): Promise<Produto[]> {
    const response = await api.post<Produto[]>('/products/consume', request);
    return response.data;
  },

  /**
   * Cria múltiplos produtos de uma vez.
   */
  async criarEmLote(produtos: ProdutoRequest[]): Promise<Produto[]> {
    const response = await api.post<Produto[]>('/products/batch', { produtos });
    return response.data;
  },

  /**
   * Lista todos os produtos expirados.
   */
  async listarExpirados(): Promise<Produto[]> {
    const response = await api.get<Produto[]>('/products/expired');
    return response.data;
  },

  /**
   * Lista produtos com estoque baixo.
   */
  async listarEstoqueBaixo(limiar?: number): Promise<Produto[]> {
    const params = limiar ? { limiar } : {};
    const response = await api.get<Produto[]>('/products/low-stock', { params });
    return response.data;
  },
};
