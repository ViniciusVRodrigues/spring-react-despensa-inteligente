import React from 'react';
import { FaTrash, FaEdit, FaMinus, FaExclamationTriangle } from 'react-icons/fa';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import type { Produto } from '../../../types';
import { categoriaDescricao } from '../../../types';
import { Badge } from '../../atoms/Badge';
import { Button } from '../../atoms/Button';
import styles from './ProductCard.module.css';

interface ProductCardProps {
  produto: Produto;
  onConsumir: (id: string, quantidade: number) => Promise<void>;
  onEditar: (produto: Produto) => void;
  onDescartar: (id: string) => Promise<void>;
}

/**
 * Card de produto que exibe informações e permite ações.
 */
export const ProductCard: React.FC<ProductCardProps> = ({
  produto,
  onConsumir,
  onEditar,
  onDescartar,
}) => {
  const [isLoading, setIsLoading] = React.useState(false);
  const [quantidadeConsumo, setQuantidadeConsumo] = React.useState(1);

  const handleConsumir = async () => {
    if (quantidadeConsumo > produto.quantidade) {
      return;
    }
    try {
      setIsLoading(true);
      await onConsumir(produto.id, quantidadeConsumo);
      setQuantidadeConsumo(1);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDescartar = async () => {
    if (window.confirm(`Deseja realmente descartar "${produto.nome}"?`)) {
      try {
        setIsLoading(true);
        await onDescartar(produto.id);
      } finally {
        setIsLoading(false);
      }
    }
  };

  const formatarData = (data: string | undefined): string => {
    if (!data) return 'Sem validade';
    return format(new Date(data), "dd 'de' MMMM 'de' yyyy", { locale: ptBR });
  };

  const getBadgeVariant = () => {
    if (produto.expirado) return 'danger';
    if (produto.quantidade <= 5) return 'warning';
    return 'success';
  };

  const getStatusText = () => {
    if (produto.expirado) return 'Expirado';
    if (produto.quantidade <= 5) return 'Estoque baixo';
    return 'Em estoque';
  };

  return (
    <div className={`${styles.card} ${produto.expirado ? styles.expired : ''}`}>
      <div className={styles.header}>
        <h3 className={styles.title}>{produto.nome}</h3>
        <Badge variant={getBadgeVariant()}>{getStatusText()}</Badge>
      </div>
      
      <div className={styles.info}>
        <div className={styles.infoRow}>
          <span className={styles.label}>Categoria:</span>
          <span className={styles.value}>{categoriaDescricao[produto.categoria]}</span>
        </div>
        <div className={styles.infoRow}>
          <span className={styles.label}>Quantidade:</span>
          <span className={`${styles.value} ${styles.quantity}`}>{produto.quantidade}</span>
        </div>
        <div className={styles.infoRow}>
          <span className={styles.label}>Validade:</span>
          <span className={`${styles.value} ${produto.expirado ? styles.expiredText : ''}`}>
            {formatarData(produto.dataValidade)}
            {produto.expirado && <FaExclamationTriangle className={styles.warningIcon} />}
          </span>
        </div>
      </div>

      <div className={styles.consumeSection}>
        <label className={styles.consumeLabel}>Consumir:</label>
        <div className={styles.consumeControls}>
          <input
            type="number"
            min="1"
            max={produto.quantidade}
            value={quantidadeConsumo}
            onChange={(e) => setQuantidadeConsumo(Math.max(1, parseInt(e.target.value) || 1))}
            className={styles.consumeInput}
            disabled={isLoading}
          />
          <Button
            variant="primary"
            size="small"
            onClick={handleConsumir}
            disabled={isLoading || quantidadeConsumo > produto.quantidade}
            loading={isLoading}
          >
            <FaMinus /> Consumir
          </Button>
        </div>
      </div>

      <div className={styles.actions}>
        <Button
          variant="secondary"
          size="small"
          onClick={() => onEditar(produto)}
          disabled={isLoading}
        >
          <FaEdit /> Editar
        </Button>
        <Button
          variant="danger"
          size="small"
          onClick={handleDescartar}
          disabled={isLoading}
        >
          <FaTrash /> Descartar
        </Button>
      </div>
    </div>
  );
};
