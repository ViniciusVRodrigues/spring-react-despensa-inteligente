import React, { useEffect, useState } from 'react';
import { Button, Badge, Loading } from '../components/atoms';
import { Card, Modal } from '../components/molecules';
import { DataTable, PantryItemForm } from '../components/organisms';
import { pantryApi, productApi } from '../services/api';
import type { PantryItem, Product, PantryItemRequest } from '../types';
import './Pantry.css';

export const Pantry: React.FC = () => {
  const [pantryItems, setPantryItems] = useState<PantryItem[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isConsumeModalOpen, setIsConsumeModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<PantryItem | null>(null);
  const [consumeQuantity, setConsumeQuantity] = useState(1);
  const [saving, setSaving] = useState(false);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [pantryResponse, productsResponse] = await Promise.all([
        pantryApi.getAll(),
        productApi.getAll(),
      ]);
      setPantryItems(pantryResponse.data);
      setProducts(productsResponse.data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar dados');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleAddItem = async (data: PantryItemRequest) => {
    try {
      setSaving(true);
      await pantryApi.add(data);
      await fetchData();
      setIsModalOpen(false);
    } catch (err) {
      alert('Erro ao adicionar item');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleConsume = async () => {
    if (!selectedItem) return;
    try {
      setSaving(true);
      await pantryApi.consume(selectedItem.id, { quantity: consumeQuantity });
      await fetchData();
      setIsConsumeModalOpen(false);
      setSelectedItem(null);
      setConsumeQuantity(1);
    } catch (err) {
      alert('Erro ao consumir item');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleDiscard = async (id: number) => {
    if (!confirm('Tem certeza que deseja descartar este item?')) return;
    try {
      await pantryApi.discard(id);
      await fetchData();
    } catch (err) {
      alert('Erro ao descartar item');
      console.error(err);
    }
  };

  const handleDiscardExpired = async () => {
    if (!confirm('Descartar todos os itens vencidos?')) return;
    try {
      await pantryApi.discardExpired();
      await fetchData();
    } catch (err) {
      alert('Erro ao descartar itens vencidos');
      console.error(err);
    }
  };

  const openConsumeModal = (item: PantryItem) => {
    setSelectedItem(item);
    setConsumeQuantity(1);
    setIsConsumeModalOpen(true);
  };

  const formatDate = (date: string | null) => {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('pt-BR');
  };

  const getStatusBadge = (item: PantryItem) => {
    if (item.isExpired) {
      return <Badge variant="danger">Vencido</Badge>;
    }
    if (item.isExpiringSoon) {
      return <Badge variant="warning">Vence em breve</Badge>;
    }
    if (item.isLowStock) {
      return <Badge variant="info">Estoque baixo</Badge>;
    }
    return <Badge variant="success">OK</Badge>;
  };

  const columns = [
    {
      key: 'product',
      header: 'Produto',
      render: (item: PantryItem) => item.product.name,
    },
    {
      key: 'quantity',
      header: 'Quantidade',
      render: (item: PantryItem) => `${item.quantity} ${item.product.unit}`,
    },
    {
      key: 'expirationDate',
      header: 'Validade',
      render: (item: PantryItem) => formatDate(item.expirationDate),
    },
    {
      key: 'location',
      header: 'Local',
      render: (item: PantryItem) => item.location || '-',
    },
    {
      key: 'status',
      header: 'Status',
      render: (item: PantryItem) => getStatusBadge(item),
    },
  ];

  if (loading) {
    return <Loading size="large" text="Carregando despensa..." />;
  }

  if (error) {
    return (
      <div className="pantry-error">
        <p>{error}</p>
        <Button onClick={fetchData}>Tentar novamente</Button>
      </div>
    );
  }

  const hasExpiredItems = pantryItems.some((item) => item.isExpired);

  return (
    <div className="pantry">
      <div className="pantry-header">
        <h1 className="page-title">Despensa</h1>
        <div className="pantry-actions">
          {hasExpiredItems && (
            <Button variant="danger" onClick={handleDiscardExpired}>
              Descartar Vencidos
            </Button>
          )}
          <Button onClick={() => setIsModalOpen(true)}>+ Adicionar Item</Button>
        </div>
      </div>

      <Card>
        <DataTable
          columns={columns}
          data={pantryItems}
          keyExtractor={(item) => item.id}
          emptyMessage="Sua despensa está vazia"
          actions={(item) => (
            <>
              <Button size="small" variant="secondary" onClick={() => openConsumeModal(item)}>
                Consumir
              </Button>
              <Button size="small" variant="danger" onClick={() => handleDiscard(item.id)}>
                Descartar
              </Button>
            </>
          )}
        />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Adicionar Item à Despensa"
      >
        <PantryItemForm
          products={products}
          onSubmit={handleAddItem}
          onCancel={() => setIsModalOpen(false)}
          isLoading={saving}
        />
      </Modal>

      <Modal
        isOpen={isConsumeModalOpen}
        onClose={() => setIsConsumeModalOpen(false)}
        title={`Consumir ${selectedItem?.product.name || ''}`}
        size="small"
      >
        <div className="consume-form">
          <p className="consume-info">
            Quantidade disponível: {selectedItem?.quantity} {selectedItem?.product.unit}
          </p>
          <div className="consume-input">
            <label>Quantidade a consumir:</label>
            <input
              type="number"
              value={consumeQuantity}
              onChange={(e) => setConsumeQuantity(parseFloat(e.target.value))}
              min={0.1}
              max={selectedItem?.quantity}
              step={0.1}
            />
          </div>
          <div className="consume-actions">
            <Button variant="secondary" onClick={() => setIsConsumeModalOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleConsume} disabled={saving}>
              {saving ? 'Consumindo...' : 'Confirmar'}
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
