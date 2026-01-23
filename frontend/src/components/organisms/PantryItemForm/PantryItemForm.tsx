import React, { useState } from 'react';
import { Input, Button } from '../../atoms';
import type { Product, PantryItemRequest } from '../../../types';
import './PantryItemForm.css';

interface PantryItemFormProps {
  products: Product[];
  initialData?: Partial<PantryItemRequest>;
  onSubmit: (data: PantryItemRequest) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

const getDefaultFormData = (
  products: Product[],
  initialData?: Partial<PantryItemRequest>
): PantryItemRequest => ({
  productId: initialData?.productId ?? (products.length > 0 ? products[0].id : 0),
  quantity: initialData?.quantity ?? 1,
  expirationDate: initialData?.expirationDate ?? '',
  location: initialData?.location ?? '',
  notes: initialData?.notes ?? '',
});

export const PantryItemForm: React.FC<PantryItemFormProps> = ({
  products,
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
}) => {
  const [formData, setFormData] = useState<PantryItemRequest>(() =>
    getDefaultFormData(products, initialData)
  );

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
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="pantry-item-form">
      <div className="form-group">
        <label className="form-label">Produto</label>
        <select
          name="productId"
          value={formData.productId}
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

      <Input
        label="Quantidade"
        type="number"
        name="quantity"
        value={formData.quantity}
        onChange={handleChange}
        required
        min={0.1}
        step={0.1}
      />

      <Input
        label="Data de Validade"
        type="date"
        name="expirationDate"
        value={formData.expirationDate || ''}
        onChange={handleChange}
      />

      <Input
        label="Localização"
        name="location"
        value={formData.location || ''}
        onChange={handleChange}
        placeholder="Ex: Geladeira, Armário"
      />

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
          {isLoading ? 'Salvando...' : 'Salvar'}
        </Button>
      </div>
    </form>
  );
};
