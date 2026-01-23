import React, { useEffect, useState } from 'react';
import { Button, Badge, Loading } from '../components/atoms';
import { Card, Modal } from '../components/molecules';
import { DataTable, ProductForm } from '../components/organisms';
import { productApi } from '../services/api';
import type { Product, ProductRequest } from '../types';
import './Products.css';

export const Products: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [saving, setSaving] = useState(false);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await productApi.getAll();
      setProducts(response.data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar produtos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleCreateProduct = async (data: ProductRequest) => {
    try {
      setSaving(true);
      await productApi.create(data);
      await fetchProducts();
      setIsModalOpen(false);
    } catch (err) {
      alert('Erro ao criar produto');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleUpdateProduct = async (data: ProductRequest) => {
    if (!editingProduct) return;
    try {
      setSaving(true);
      await productApi.update(editingProduct.id, data);
      await fetchProducts();
      setEditingProduct(null);
      setIsModalOpen(false);
    } catch (err) {
      alert('Erro ao atualizar produto');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleDeleteProduct = async (id: number) => {
    if (!confirm('Tem certeza que deseja excluir este produto?')) return;
    try {
      await productApi.delete(id);
      await fetchProducts();
    } catch (err) {
      alert('Erro ao excluir produto');
      console.error(err);
    }
  };

  const openCreateModal = () => {
    setEditingProduct(null);
    setIsModalOpen(true);
  };

  const openEditModal = (product: Product) => {
    setEditingProduct(product);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingProduct(null);
  };

  const columns = [
    { key: 'name', header: 'Nome' },
    { key: 'category', header: 'Categoria' },
    { key: 'unit', header: 'Unidade' },
    {
      key: 'trackExpiration',
      header: 'Validade',
      render: (product: Product) => (
        <Badge variant={product.trackExpiration ? 'success' : 'default'}>
          {product.trackExpiration ? 'Sim' : 'Não'}
        </Badge>
      ),
    },
  ];

  if (loading) {
    return <Loading size="large" text="Carregando produtos..." />;
  }

  if (error) {
    return (
      <div className="products-error">
        <p>{error}</p>
        <Button onClick={fetchProducts}>Tentar novamente</Button>
      </div>
    );
  }

  return (
    <div className="products">
      <div className="products-header">
        <h1 className="page-title">Produtos</h1>
        <Button onClick={openCreateModal}>+ Novo Produto</Button>
      </div>

      <Card>
        <DataTable
          columns={columns}
          data={products}
          keyExtractor={(product) => product.id}
          emptyMessage="Nenhum produto cadastrado"
          actions={(product) => (
            <>
              <Button size="small" variant="secondary" onClick={() => openEditModal(product)}>
                Editar
              </Button>
              <Button size="small" variant="danger" onClick={() => handleDeleteProduct(product.id)}>
                Excluir
              </Button>
            </>
          )}
        />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={closeModal}
        title={editingProduct ? 'Editar Produto' : 'Novo Produto'}
      >
        <ProductForm
          initialData={
            editingProduct
              ? {
                  name: editingProduct.name,
                  category: editingProduct.category,
                  unit: editingProduct.unit,
                  description: editingProduct.description,
                  trackExpiration: editingProduct.trackExpiration,
                }
              : undefined
          }
          onSubmit={editingProduct ? handleUpdateProduct : handleCreateProduct}
          onCancel={closeModal}
          isLoading={saving}
        />
      </Modal>
    </div>
  );
};
