import React from 'react';
import { FaTrash, FaCheck } from 'react-icons/fa';
import type { ItemListaCompras } from '../../../types';
import { categoriaDescricao } from '../../../types';
import { Button } from '../../atoms/Button';
import styles from './ShoppingListItem.module.css';

interface ShoppingListItemProps {
  item: ItemListaCompras;
  onRemover: (id: string) => Promise<void>;
}

/**
 * Item da lista de compras com opções de ação.
 */
export const ShoppingListItem: React.FC<ShoppingListItemProps> = ({
  item,
  onRemover,
}) => {
  const [isLoading, setIsLoading] = React.useState(false);

  const handleRemover = async () => {
    try {
      setIsLoading(true);
      await onRemover(item.id);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={`${styles.item} ${item.comprado ? styles.comprado : ''}`}>
      <div className={styles.checkboxContainer}>
        <div className={`${styles.checkbox} ${item.comprado ? styles.checked : ''}`}>
          {item.comprado && <FaCheck />}
        </div>
      </div>
      
      <div className={styles.content}>
        <div className={styles.header}>
          <span className={styles.nome}>{item.nome}</span>
          <span className={styles.quantidade}>x{item.quantidade}</span>
        </div>
        {item.categoria && (
          <span className={styles.categoria}>
            {categoriaDescricao[item.categoria]}
          </span>
        )}
      </div>
      
      <div className={styles.actions}>
        <Button
          variant="danger"
          size="small"
          onClick={handleRemover}
          disabled={isLoading}
          loading={isLoading}
        >
          <FaTrash />
        </Button>
      </div>
    </div>
  );
};
