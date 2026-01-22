import React from 'react';
import { FaShoppingCart } from 'react-icons/fa';
import type { Produto } from '../../../types';
import { categoriaDescricao } from '../../../types';
import { Badge } from '../../atoms/Badge';
import { Button } from '../../atoms/Button';
import styles from './AlertCard.module.css';

type AlertType = 'estoque-baixo' | 'expirado' | 'expirando';

interface AlertCardProps {
  produto: Produto;
  alertType: AlertType;
  onAdicionarLista: (produto: Produto) => Promise<void>;
}

/**
 * Card de alerta para produtos com estoque baixo ou expirando.
 */
export const AlertCard: React.FC<AlertCardProps> = ({
  produto,
  alertType,
  onAdicionarLista,
}) => {
  const [isLoading, setIsLoading] = React.useState(false);

  const handleAdicionarLista = async () => {
    try {
      setIsLoading(true);
      await onAdicionarLista(produto);
    } finally {
      setIsLoading(false);
    }
  };

  const getBadgeVariant = () => {
    switch (alertType) {
      case 'expirado':
        return 'danger';
      case 'expirando':
        return 'warning';
      case 'estoque-baixo':
        return 'warning';
      default:
        return 'default';
    }
  };

  const getAlertLabel = () => {
    switch (alertType) {
      case 'expirado':
        return 'Expirado';
      case 'expirando':
        return 'Expirando';
      case 'estoque-baixo':
        return 'Estoque Baixo';
      default:
        return '';
    }
  };

  const formatarData = (data: string | undefined): string => {
    if (!data) return 'Sem validade';
    const date = new Date(data);
    return date.toLocaleDateString('pt-BR');
  };

  return (
    <div className={`${styles.card} ${styles[alertType]}`}>
      <div className={styles.content}>
        <div className={styles.header}>
          <h4 className={styles.title}>{produto.nome}</h4>
          <Badge variant={getBadgeVariant()}>{getAlertLabel()}</Badge>
        </div>
        <div className={styles.details}>
          <span className={styles.detail}>
            <strong>Categoria:</strong> {categoriaDescricao[produto.categoria]}
          </span>
          <span className={styles.detail}>
            <strong>Quantidade:</strong> {produto.quantidade}
          </span>
          {produto.dataValidade && (
            <span className={styles.detail}>
              <strong>Validade:</strong> {formatarData(produto.dataValidade)}
            </span>
          )}
        </div>
      </div>
      <div className={styles.action}>
        <Button
          variant="primary"
          size="small"
          onClick={handleAdicionarLista}
          loading={isLoading}
          disabled={isLoading}
        >
          <FaShoppingCart /> Adicionar à Lista
        </Button>
      </div>
    </div>
  );
};
