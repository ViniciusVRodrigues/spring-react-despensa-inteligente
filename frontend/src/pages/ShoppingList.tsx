import React, { useEffect, useState, useCallback } from 'react';
import { Button, Badge, Loading } from '../components/atoms';
import { Card, Modal } from '../components/molecules';
import { DataTable, ShoppingListItemForm } from '../components/organisms';
import { shoppingListApi, productApi } from '../services/api';
import type { ShoppingListItem, Product, ShoppingListItemRequest, Priority } from '../types';
import './ShoppingList.css';

const priorityLabels: Record<Priority, string> = {
  LOW: 'Baixa',
  MEDIUM: 'Média',
  HIGH: 'Alta',
  URGENT: 'Urgente',
};

const priorityVariants: Record<Priority, 'default' | 'success' | 'warning' | 'danger' | 'info'> = {
  LOW: 'default',
  MEDIUM: 'info',
  HIGH: 'warning',
  URGENT: 'danger',
};

export const ShoppingList: React.FC = () => {
  const [items, setItems] = useState<ShoppingListItem[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [showAll, setShowAll] = useState(false);

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const [itemsResponse, productsResponse] = await Promise.all([
        showAll ? shoppingListApi.getAll() : shoppingListApi.getPending(),
        productApi.getAll(),
      ]);
      setItems(itemsResponse.data);
      setProducts(productsResponse.data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar lista de compras');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [showAll]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleAddItem = async (data: ShoppingListItemRequest) => {
    try {
      setSaving(true);
      await shoppingListApi.add(data);
      await fetchData();
      setIsModalOpen(false);
    } catch (err) {
      alert('Erro ao adicionar item');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleMarkAsPurchased = async (id: number) => {
    try {
      await shoppingListApi.markAsPurchased(id);
      await fetchData();
    } catch (err) {
      alert('Erro ao marcar como comprado');
      console.error(err);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Remover este item da lista?')) return;
    try {
      await shoppingListApi.delete(id);
      await fetchData();
    } catch (err) {
      alert('Erro ao remover item');
      console.error(err);
    }
  };

  const handleClearPurchased = async () => {
    if (!confirm('Limpar todos os itens comprados?')) return;
    try {
      await shoppingListApi.clearPurchased();
      await fetchData();
    } catch (err) {
      alert('Erro ao limpar itens');
      console.error(err);
    }
  };

  const formatDateTime = (date: string) => {
    return new Date(date).toLocaleString('pt-BR');
  };

  const columns = [
    {
      key: 'product',
      header: 'Produto',
      render: (item: ShoppingListItem) => (
        <div className="product-cell">
          <span className={item.status === 'PURCHASED' ? 'purchased' : ''}>
            {item.product.name}
          </span>
          {item.autoAdded && (
            <Badge variant="info" size="small">
              Auto
            </Badge>
          )}
        </div>
      ),
    },
    {
      key: 'quantity',
      header: 'Quantidade',
      render: (item: ShoppingListItem) => `${item.quantity} ${item.product.unit}`,
    },
    {
      key: 'priority',
      header: 'Prioridade',
      render: (item: ShoppingListItem) => (
        <Badge variant={priorityVariants[item.priority]}>
          {priorityLabels[item.priority]}
        </Badge>
      ),
    },
    {
      key: 'status',
      header: 'Status',
      render: (item: ShoppingListItem) => (
        <Badge variant={item.status === 'PURCHASED' ? 'success' : 'default'}>
          {item.status === 'PURCHASED' ? 'Comprado' : item.status === 'CANCELLED' ? 'Cancelado' : 'Pendente'}
        </Badge>
      ),
    },
    {
      key: 'addedAt',
      header: 'Adicionado em',
      render: (item: ShoppingListItem) => formatDateTime(item.addedAt),
    },
  ];

  if (loading) {
    return <Loading size="large" text="Carregando lista de compras..." />;
  }

  if (error) {
    return (
      <div className="shopping-list-error">
        <p>{error}</p>
        <Button onClick={fetchData}>Tentar novamente</Button>
      </div>
    );
  }

  const hasPurchasedItems = items.some((item) => item.status === 'PURCHASED');

  return (
    <div className="shopping-list">
      <div className="shopping-list-header">
        <h1 className="page-title">Lista de Compras</h1>
        <div className="shopping-list-actions">
          <label className="show-all-toggle">
            <input
              type="checkbox"
              checked={showAll}
              onChange={(e) => setShowAll(e.target.checked)}
            />
            Mostrar todos
          </label>
          {hasPurchasedItems && (
            <Button variant="secondary" onClick={handleClearPurchased}>
              Limpar Comprados
            </Button>
          )}
          <Button onClick={() => setIsModalOpen(true)}>+ Adicionar Item</Button>
        </div>
      </div>

      <Card>
        <DataTable
          columns={columns}
          data={items}
          keyExtractor={(item) => item.id}
          emptyMessage="Lista de compras vazia"
          actions={(item) =>
            item.status === 'PENDING' ? (
              <>
                <Button
                  size="small"
                  variant="success"
                  onClick={() => handleMarkAsPurchased(item.id)}
                >
                  ✓ Comprado
                </Button>
                <Button size="small" variant="danger" onClick={() => handleDelete(item.id)}>
                  Remover
                </Button>
              </>
            ) : null
          }
        />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Adicionar à Lista de Compras"
      >
        <ShoppingListItemForm
          products={products}
          onSubmit={handleAddItem}
          onCancel={() => setIsModalOpen(false)}
          isLoading={saving}
        />
      </Modal>
    </div>
  );
};
