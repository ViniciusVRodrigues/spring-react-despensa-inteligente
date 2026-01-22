import React, { useState, useEffect, useCallback } from 'react';
import toast from 'react-hot-toast';
import { FaPlus, FaExclamationTriangle } from 'react-icons/fa';
import type { Produto, ProdutoRequest, CategoriaProduto } from '../../../types';
import { produtoService } from '../../../services/produtoService';
import { ProductList } from '../../organisms/ProductList';
import { ProductForm } from '../../organisms/ProductForm';
import { Button } from '../../atoms/Button';
import { Badge } from '../../atoms/Badge';
import styles from './DespensaPage.module.css';

/**
 * Página principal da Despensa.
 */
export const DespensaPage: React.FC = () => {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [produtosExpirados, setProdutosExpirados] = useState<Produto[]>([]);
  const [produtosEstoqueBaixo, setProdutosEstoqueBaixo] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);
  const [categoriaFiltro, setCategoriaFiltro] = useState<CategoriaProduto | ''>('');
  const [showForm, setShowForm] = useState(false);
  const [produtoEdit, setProdutoEdit] = useState<Produto | null>(null);

  const carregarProdutos = useCallback(async () => {
    try {
      setLoading(true);
      const [todosProdutos, expirados, estoqueBaixo] = await Promise.all([
        produtoService.listarTodos(categoriaFiltro || undefined),
        produtoService.listarExpirados(),
        produtoService.listarEstoqueBaixo(5),
      ]);
      setProdutos(todosProdutos);
      setProdutosExpirados(expirados);
      setProdutosEstoqueBaixo(estoqueBaixo);
    } catch (error) {
      toast.error('Erro ao carregar produtos');
      console.error('Erro ao carregar produtos:', error);
    } finally {
      setLoading(false);
    }
  }, [categoriaFiltro]);

  useEffect(() => {
    carregarProdutos();
  }, [carregarProdutos]);

  const handleCriarProduto = async (produto: ProdutoRequest) => {
    try {
      await produtoService.criar(produto);
      toast.success('Produto cadastrado com sucesso!');
      setShowForm(false);
      await carregarProdutos();
    } catch (error) {
      toast.error('Erro ao cadastrar produto');
      console.error('Erro ao cadastrar produto:', error);
    }
  };

  const handleAtualizarProduto = async (produto: ProdutoRequest) => {
    if (!produtoEdit) return;
    try {
      await produtoService.atualizar(produtoEdit.id, produto);
      toast.success('Produto atualizado com sucesso!');
      setProdutoEdit(null);
      await carregarProdutos();
    } catch (error) {
      toast.error('Erro ao atualizar produto');
      console.error('Erro ao atualizar produto:', error);
    }
  };

  const handleConsumir = async (id: string, quantidade: number) => {
    try {
      await produtoService.consumirEmLote({
        consumos: [{ produtoId: id, quantidade }],
      });
      toast.success(`Consumo de ${quantidade} unidade(s) registrado!`);
      await carregarProdutos();
    } catch (error) {
      toast.error('Erro ao consumir produto');
      console.error('Erro ao consumir produto:', error);
    }
  };

  const handleDescartar = async (id: string) => {
    try {
      await produtoService.descartar(id);
      toast.success('Produto descartado com sucesso!');
      await carregarProdutos();
    } catch (error) {
      toast.error('Erro ao descartar produto');
      console.error('Erro ao descartar produto:', error);
    }
  };

  const handleEditar = (produto: Produto) => {
    setProdutoEdit(produto);
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h2 className={styles.title}>Minha Despensa</h2>
          <div className={styles.stats}>
            <span className={styles.stat}>
              {produtos.length} produto(s) na despensa
            </span>
            {produtosExpirados.length > 0 && (
              <Badge variant="danger">
                <FaExclamationTriangle /> {produtosExpirados.length} expirado(s)
              </Badge>
            )}
            {produtosEstoqueBaixo.length > 0 && (
              <Badge variant="warning">
                {produtosEstoqueBaixo.length} com estoque baixo
              </Badge>
            )}
          </div>
        </div>
        <Button variant="success" onClick={() => setShowForm(true)}>
          <FaPlus /> Novo Produto
        </Button>
      </div>

      <ProductList
        produtos={produtos}
        loading={loading}
        categoriaFiltro={categoriaFiltro}
        onCategoriaChange={setCategoriaFiltro}
        onConsumir={handleConsumir}
        onEditar={handleEditar}
        onDescartar={handleDescartar}
      />

      {showForm && (
        <ProductForm
          onSubmit={handleCriarProduto}
          onCancel={() => setShowForm(false)}
        />
      )}

      {produtoEdit && (
        <ProductForm
          produtoEdit={produtoEdit}
          onSubmit={handleAtualizarProduto}
          onCancel={() => setProdutoEdit(null)}
        />
      )}
    </div>
  );
};
