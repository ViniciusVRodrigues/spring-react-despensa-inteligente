import React from 'react';
import { useForm } from 'react-hook-form';
import { FaMagic, FaPlus } from 'react-icons/fa';
import type { ItemListaCompras, ItemListaComprasRequest } from '../../../types';
import { CategoriaProduto, categoriaDescricao } from '../../../types';
import { ShoppingListItem } from '../../molecules/ShoppingListItem';
import { Button } from '../../atoms/Button';
import { Input, Select } from '../../atoms/Input';
import { Loading } from '../../atoms/Loading';
import styles from './ShoppingList.module.css';

interface ShoppingListProps {
  itens: ItemListaCompras[];
  loading: boolean;
  onAdicionarItem: (item: ItemListaComprasRequest) => Promise<void>;
  onRemoverItem: (id: string) => Promise<void>;
  onGerarAutomatica: () => Promise<void>;
}

/**
 * Componente da lista de compras com formulário de adição.
 */
export const ShoppingList: React.FC<ShoppingListProps> = ({
  itens,
  loading,
  onAdicionarItem,
  onRemoverItem,
  onGerarAutomatica,
}) => {
  const [isAdding, setIsAdding] = React.useState(false);
  const [isGenerating, setIsGenerating] = React.useState(false);
  const [showForm, setShowForm] = React.useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ItemListaComprasRequest>({
    defaultValues: {
      nome: '',
      quantidade: 1,
      categoria: undefined,
    },
  });

  const categoriaOptions = [
    { value: '', label: 'Selecionar categoria (opcional)' },
    ...Object.values(CategoriaProduto).map((cat) => ({
      value: cat,
      label: categoriaDescricao[cat],
    })),
  ];

  const handleAddItem = async (data: ItemListaComprasRequest) => {
    try {
      setIsAdding(true);
      await onAdicionarItem({
        ...data,
        categoria: data.categoria || undefined,
      });
      reset();
      setShowForm(false);
    } finally {
      setIsAdding(false);
    }
  };

  const handleGerarAutomatica = async () => {
    try {
      setIsGenerating(true);
      await onGerarAutomatica();
    } finally {
      setIsGenerating(false);
    }
  };

  if (loading) {
    return <Loading text="Carregando lista de compras..." />;
  }

  return (
    <div className={styles.container}>
      <div className={styles.actions}>
        <Button
          variant="primary"
          onClick={() => setShowForm(!showForm)}
        >
          <FaPlus /> Adicionar Item
        </Button>
        <Button
          variant="secondary"
          onClick={handleGerarAutomatica}
          loading={isGenerating}
        >
          <FaMagic /> Gerar Automaticamente
        </Button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit(handleAddItem)} className={styles.form}>
          <div className={styles.formRow}>
            <Input
              label="Nome do Item *"
              placeholder="Ex: Arroz"
              error={errors.nome?.message}
              fullWidth
              {...register('nome', {
                required: 'O nome é obrigatório',
              })}
            />
            <Input
              type="number"
              label="Quantidade *"
              min="1"
              error={errors.quantidade?.message}
              {...register('quantidade', {
                required: 'A quantidade é obrigatória',
                min: { value: 1, message: 'A quantidade deve ser maior que zero' },
                valueAsNumber: true,
              })}
            />
          </div>
          <Select
            label="Categoria"
            options={categoriaOptions}
            {...register('categoria')}
          />
          <div className={styles.formActions}>
            <Button
              type="button"
              variant="secondary"
              size="small"
              onClick={() => {
                setShowForm(false);
                reset();
              }}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="success"
              size="small"
              loading={isAdding}
            >
              Adicionar
            </Button>
          </div>
        </form>
      )}

      {itens.length === 0 ? (
        <div className={styles.empty}>
          <p>Sua lista de compras está vazia.</p>
          <p className={styles.emptyHint}>
            Adicione itens manualmente ou gere automaticamente baseado no estoque baixo.
          </p>
        </div>
      ) : (
        <div className={styles.list}>
          {itens.map((item) => (
            <ShoppingListItem
              key={item.id}
              item={item}
              onRemover={onRemoverItem}
            />
          ))}
        </div>
      )}
    </div>
  );
};
