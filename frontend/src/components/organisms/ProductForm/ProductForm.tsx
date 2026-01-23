import React, { useState } from 'react';
import { Input, Button } from '../../atoms';
import type { ProductRequest } from '../../../types';
import './ProductForm.css';

interface ProductFormProps {
  initialData?: ProductRequest;
  onSubmit: (data: ProductRequest) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

const CATEGORIES = [
  'Alimentos',
  'Bebidas',
  'Laticínios',
  'Carnes',
  'Frutas',
  'Vegetais',
  'Congelados',
  'Limpeza',
  'Higiene',
  'Outros',
];

const UNITS = ['un', 'kg', 'g', 'L', 'ml', 'pacote', 'caixa', 'lata', 'garrafa'];

const getDefaultFormData = (initialData?: ProductRequest): ProductRequest => ({
  name: initialData?.name ?? '',
  category: initialData?.category ?? 'Alimentos',
  unit: initialData?.unit ?? 'un',
  description: initialData?.description ?? '',
  trackExpiration: initialData?.trackExpiration ?? true,
});

export const ProductForm: React.FC<ProductFormProps> = ({
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
}) => {
  const [formData, setFormData] = useState<ProductRequest>(() => getDefaultFormData(initialData));

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="product-form">
      <Input
        label="Nome do Produto"
        name="name"
        value={formData.name}
        onChange={handleChange}
        required
        placeholder="Ex: Arroz"
      />

      <div className="form-group">
        <label className="form-label">Categoria</label>
        <select
          name="category"
          value={formData.category}
          onChange={handleChange}
          className="form-select"
        >
          {CATEGORIES.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Unidade</label>
        <select
          name="unit"
          value={formData.unit}
          onChange={handleChange}
          className="form-select"
        >
          {UNITS.map((unit) => (
            <option key={unit} value={unit}>
              {unit}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Descrição</label>
        <textarea
          name="description"
          value={formData.description || ''}
          onChange={handleChange}
          className="form-textarea"
          placeholder="Descrição opcional do produto"
          rows={3}
        />
      </div>

      <div className="form-checkbox">
        <input
          type="checkbox"
          id="trackExpiration"
          name="trackExpiration"
          checked={formData.trackExpiration}
          onChange={handleChange}
        />
        <label htmlFor="trackExpiration">Controlar data de validade</label>
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
