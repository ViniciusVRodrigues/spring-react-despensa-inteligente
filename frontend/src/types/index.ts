/**
 * Tipos TypeScript para o Smart Pantry
 */

/**
 * Constante que representa as categorias de produtos disponíveis na despensa.
 */
export const CategoriaProduto = {
  GRAOS: 'GRAOS',
  LATICINIOS: 'LATICINIOS',
  BEBIDAS: 'BEBIDAS',
  ENLATADOS: 'ENLATADOS',
  CONGELADOS: 'CONGELADOS',
  OUTROS: 'OUTROS'
} as const;

export type CategoriaProduto = typeof CategoriaProduto[keyof typeof CategoriaProduto];

/**
 * Mapeamento de categorias para descrições em português.
 */
export const categoriaDescricao: Record<CategoriaProduto, string> = {
  [CategoriaProduto.GRAOS]: 'Grãos',
  [CategoriaProduto.LATICINIOS]: 'Laticínios',
  [CategoriaProduto.BEBIDAS]: 'Bebidas',
  [CategoriaProduto.ENLATADOS]: 'Enlatados',
  [CategoriaProduto.CONGELADOS]: 'Congelados',
  [CategoriaProduto.OUTROS]: 'Outros'
};

/**
 * Interface que representa um produto na despensa.
 */
export interface Produto {
  id: string;
  nome: string;
  quantidade: number;
  dataValidade?: string; // ISO 8601
  categoria: CategoriaProduto;
  expirado: boolean;
  ativo: boolean;
  criadoEm: string;
  atualizadoEm: string;
}

/**
 * Interface para criação/atualização de produto.
 */
export interface ProdutoRequest {
  nome: string;
  quantidade: number;
  dataValidade?: string;
  categoria: CategoriaProduto;
}

/**
 * Interface para consumo de produto.
 */
export interface ConsumoRequest {
  produtoId: string;
  quantidade: number;
}

/**
 * Interface para consumo em lote.
 */
export interface ConsumoLoteRequest {
  consumos: ConsumoRequest[];
}

/**
 * Interface que representa um item na lista de compras.
 */
export interface ItemListaCompras {
  id: string;
  nome: string;
  quantidade: number;
  categoria?: CategoriaProduto;
  comprado: boolean;
  criadoEm: string;
  atualizadoEm: string;
}

/**
 * Interface para criação de item na lista de compras.
 */
export interface ItemListaComprasRequest {
  nome: string;
  quantidade: number;
  categoria?: CategoriaProduto;
}
