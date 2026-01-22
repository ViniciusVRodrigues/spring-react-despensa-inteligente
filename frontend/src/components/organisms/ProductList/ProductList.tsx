import React from 'react';
import type { Produto, CategoriaProduto } from '../../../types';
import { CategoriaProduto as CategoriaProdutoValues, categoriaDescricao } from '../../../types';
import { ProductCard } from '../../molecules/ProductCard';
import { Loading } from '../../atoms/Loading';
import { Select } from '../../atoms/Input';
import styles from './ProductList.module.css';

interface ProductListProps {
  produtos: Produto[];
  loading: boolean;
  categoriaFiltro: CategoriaProduto | '';
  onCategoriaChange: (categoria: CategoriaProduto | '') => void;
  onConsumir: (id: string, quantidade: number) => Promise<void>;
  onEditar: (produto: Produto) => void;
  onDescartar: (id: string) => Promise<void>;
}

/**
 * Lista de produtos com filtro por categoria.
 */
export const ProductList: React.FC<ProductListProps> = ({
  produtos,
  loading,
  categoriaFiltro,
  onCategoriaChange,
  onConsumir,
  onEditar,
  onDescartar,
}) => {
  const categoriaOptions = [
    { value: '', label: 'Todas as categorias' },
    ...Object.values(CategoriaProdutoValues).map((cat) => ({
      value: cat,
      label: categoriaDescricao[cat],
    })),
  ];

  if (loading) {
    return <Loading text="Carregando produtos..." />;
  }

  return (
    <div className={styles.container}>
      <div className={styles.filters}>
        <Select
          label="Filtrar por categoria"
          options={categoriaOptions}
          value={categoriaFiltro}
          onChange={(e) => onCategoriaChange(e.target.value as CategoriaProduto | '')}
        />
      </div>

      {produtos.length === 0 ? (
        <div className={styles.empty}>
          <p>Nenhum produto encontrado na despensa.</p>
          <p className={styles.emptyHint}>
            Adicione produtos clicando em "Novo Produto".
          </p>
        </div>
      ) : (
        <div className={styles.grid}>
          {produtos.map((produto) => (
            <ProductCard
              key={produto.id}
              produto={produto}
              onConsumir={onConsumir}
              onEditar={onEditar}
              onDescartar={onDescartar}
            />
          ))}
        </div>
      )}
    </div>
  );
};
