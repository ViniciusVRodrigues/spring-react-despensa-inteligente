import React from 'react';
import { useForm } from 'react-hook-form';
import { FaTimes, FaSave } from 'react-icons/fa';
import type { ProdutoRequest, Produto } from '../../../types';
import { CategoriaProduto, categoriaDescricao } from '../../../types';
import { Input, Select } from '../../atoms/Input';
import { Button } from '../../atoms/Button';
import styles from './ProductForm.module.css';

interface ProductFormProps {
  produtoEdit?: Produto | null;
  onSubmit: (produto: ProdutoRequest) => Promise<void>;
  onCancel: () => void;
}

/**
 * Formulário para criação e edição de produtos.
 */
export const ProductForm: React.FC<ProductFormProps> = ({
  produtoEdit,
  onSubmit,
  onCancel,
}) => {
  const [isLoading, setIsLoading] = React.useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ProdutoRequest>({
    defaultValues: produtoEdit
      ? {
          nome: produtoEdit.nome,
          quantidade: produtoEdit.quantidade,
          dataValidade: produtoEdit.dataValidade?.split('T')[0] || '',
          categoria: produtoEdit.categoria,
        }
      : {
          nome: '',
          quantidade: 1,
          dataValidade: '',
          categoria: CategoriaProduto.OUTROS,
        },
  });

  const categoriaOptions = Object.values(CategoriaProduto).map((cat) => ({
    value: cat,
    label: categoriaDescricao[cat],
  }));

  const handleFormSubmit = async (data: ProdutoRequest) => {
    try {
      setIsLoading(true);
      const produto: ProdutoRequest = {
        ...data,
        dataValidade: data.dataValidade || undefined,
      };
      await onSubmit(produto);
      reset();
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onCancel}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.header}>
          <h2 className={styles.title}>
            {produtoEdit ? 'Editar Produto' : 'Novo Produto'}
          </h2>
          <button className={styles.closeButton} onClick={onCancel}>
            <FaTimes />
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className={styles.form}>
          <Input
            label="Nome do Produto *"
            placeholder="Ex: Arroz"
            error={errors.nome?.message}
            fullWidth
            {...register('nome', {
              required: 'O nome do produto é obrigatório',
            })}
          />

          <div className={styles.row}>
            <Input
              type="number"
              label="Quantidade *"
              min="0"
              error={errors.quantidade?.message}
              fullWidth
              {...register('quantidade', {
                required: 'A quantidade é obrigatória',
                min: { value: 0, message: 'A quantidade deve ser maior ou igual a zero' },
                valueAsNumber: true,
              })}
            />

            <Input
              type="date"
              label="Data de Validade"
              error={errors.dataValidade?.message}
              fullWidth
              {...register('dataValidade')}
            />
          </div>

          <Select
            label="Categoria *"
            options={categoriaOptions}
            error={errors.categoria?.message}
            fullWidth
            {...register('categoria', {
              required: 'A categoria é obrigatória',
            })}
          />

          <div className={styles.actions}>
            <Button
              type="button"
              variant="secondary"
              onClick={onCancel}
              disabled={isLoading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="success"
              loading={isLoading}
            >
              <FaSave /> {produtoEdit ? 'Salvar Alterações' : 'Cadastrar'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};
