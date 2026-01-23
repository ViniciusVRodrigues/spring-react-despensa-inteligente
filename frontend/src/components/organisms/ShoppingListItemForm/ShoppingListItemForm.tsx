import React, { useState } from 'react';
import { Input, Button } from '../../atoms';
import type { Product, ShoppingListItemRequest, Priority } from '../../../types';
import './ShoppingListItemForm.css';

interface ShoppingListItemFormProps {
  products: Product[];
  initialData?: Partial<ShoppingListItemRequest>;
  onSubmit: (data: ShoppingListItemRequest) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

const PRIORITIES: { value: Priority; label: string }[] = [
  { value: 'LOW', label: 'Baixa' },
  { value: 'MEDIUM', label: 'Média' },
  { value: 'HIGH', label: 'Alta' },
  { value: 'URGENT', label: 'Urgente' },
];

const getDefaultFormData = (
  products: Product[],
  initialData?: Partial<ShoppingListItemRequest>
): ShoppingListItemRequest => ({
  productId: initialData?.productId ?? (products.length > 0 ? products[0].id : undefined),
  productName: initialData?.productName ?? '',
  quantity: initialData?.quantity ?? 1,
  priority: initialData?.priority ?? 'MEDIUM',
  notes: initialData?.notes ?? '',
});

export const ShoppingListItemForm: React.FC<ShoppingListItemFormProps> = ({
  products,
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
}) => {
  const [formData, setFormData] = useState<ShoppingListItemRequest>(() =>
    getDefaultFormData(products, initialData)
  );
  const [useExistingProduct, setUseExistingProduct] = useState(true);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? parseFloat(value) : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const submitData: ShoppingListItemRequest = {
      quantity: formData.quantity,
      priority: formData.priority,
      notes: formData.notes,
    };
    
    if (useExistingProduct) {
      submitData.productId = formData.productId;
    } else {
      submitData.productName = formData.productName;
    }
    
    onSubmit(submitData);
  };

  return (
    <form onSubmit={handleSubmit} className="shopping-list-item-form">
      <div className="form-toggle">
        <button
          type="button"
          className={`toggle-btn ${useExistingProduct ? 'active' : ''}`}
          onClick={() => setUseExistingProduct(true)}
        >
          Produto Existente
        </button>
        <button
          type="button"
          className={`toggle-btn ${!useExistingProduct ? 'active' : ''}`}
          onClick={() => setUseExistingProduct(false)}
        >
          Novo Produto
        </button>
      </div>

      {useExistingProduct ? (
        <div className="form-group">
          <label className="form-label">Produto</label>
          <select
            name="productId"
            value={formData.productId || ''}
            onChange={handleChange}
            className="form-select"
            required
          >
            <option value="">Selecione um produto</option>
            {products.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name} ({product.unit})
              </option>
            ))}
          </select>
        </div>
      ) : (
        <Input
          label="Nome do Produto"
          name="productName"
          value={formData.productName || ''}
          onChange={handleChange}
          required
          placeholder="Ex: Leite"
        />
      )}

      <Input
        label="Quantidade"
        type="number"
        name="quantity"
        value={formData.quantity || 1}
        onChange={handleChange}
        required
        min={0.1}
        step={0.1}
      />

      <div className="form-group">
        <label className="form-label">Prioridade</label>
        <select
          name="priority"
          value={formData.priority}
          onChange={handleChange}
          className="form-select"
        >
          {PRIORITIES.map((p) => (
            <option key={p.value} value={p.value}>
              {p.label}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Observações</label>
        <textarea
          name="notes"
          value={formData.notes || ''}
          onChange={handleChange}
          className="form-textarea"
          placeholder="Observações sobre o item"
          rows={2}
        />
      </div>

      <div className="form-actions">
        <Button type="button" variant="secondary" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? 'Salvando...' : 'Adicionar'}
        </Button>
      </div>
    </form>
  );
};
