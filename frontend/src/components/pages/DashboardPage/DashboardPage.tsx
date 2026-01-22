import React, { useState, useEffect, useCallback } from 'react';
import toast from 'react-hot-toast';
import { FaBox, FaExclamationTriangle, FaClipboardList, FaWarehouse } from 'react-icons/fa';
import type { Produto, ItemListaComprasRequest } from '../../../types';
import { CategoriaProduto, categoriaDescricao } from '../../../types';
import { produtoService } from '../../../services/produtoService';
import { listaComprasService } from '../../../services/listaComprasService';
import { AlertCard } from '../../molecules/AlertCard';
import { Loading } from '../../atoms/Loading';
import styles from './DashboardPage.module.css';

/**
 * Dashboard - Página principal com visão geral da despensa.
 */
export const DashboardPage: React.FC = () => {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [produtosExpirados, setProdutosExpirados] = useState<Produto[]>([]);
  const [produtosEstoqueBaixo, setProdutosEstoqueBaixo] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);

  const carregarDados = useCallback(async () => {
    try {
      setLoading(true);
      const [todosProdutos, expirados, estoqueBaixo] = await Promise.all([
        produtoService.listarTodos(),
        produtoService.listarExpirados(),
        produtoService.listarEstoqueBaixo(5),
      ]);
      setProdutos(todosProdutos);
      setProdutosExpirados(expirados);
      setProdutosEstoqueBaixo(estoqueBaixo);
    } catch (error) {
      toast.error('Erro ao carregar dados do dashboard');
      console.error('Erro ao carregar dados:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    carregarDados();
  }, [carregarDados]);

  const handleAdicionarLista = async (produto: Produto) => {
    try {
      const item: ItemListaComprasRequest = {
        nome: produto.nome,
        quantidade: Math.max(5, 10 - produto.quantidade),
        categoria: produto.categoria,
      };
      await listaComprasService.adicionar(item);
      toast.success(`"${produto.nome}" adicionado à lista de compras!`);
    } catch (error) {
      toast.error('Erro ao adicionar à lista de compras');
      console.error('Erro ao adicionar à lista:', error);
    }
  };

  // Calcular estatísticas por categoria
  const produtosPorCategoria = produtos.reduce((acc, prod) => {
    acc[prod.categoria] = (acc[prod.categoria] || 0) + 1;
    return acc;
  }, {} as Record<string, number>);

  const quantidadeTotal = produtos.reduce((acc, prod) => acc + prod.quantidade, 0);

  if (loading) {
    return <Loading text="Carregando dashboard..." />;
  }

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Dashboard</h2>
      <p className={styles.subtitle}>Visão geral da sua despensa inteligente</p>

      {/* Cards de Estatísticas */}
      <div className={styles.statsGrid}>
        <div className={styles.statCard}>
          <div className={styles.statIcon}>
            <FaBox />
          </div>
          <div className={styles.statContent}>
            <span className={styles.statValue}>{produtos.length}</span>
            <span className={styles.statLabel}>Produtos na Despensa</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={styles.statIcon}>
            <FaWarehouse />
          </div>
          <div className={styles.statContent}>
            <span className={styles.statValue}>{quantidadeTotal}</span>
            <span className={styles.statLabel}>Itens em Estoque</span>
          </div>
        </div>

        <div className={`${styles.statCard} ${produtosEstoqueBaixo.length > 0 ? styles.warning : ''}`}>
          <div className={styles.statIcon}>
            <FaClipboardList />
          </div>
          <div className={styles.statContent}>
            <span className={styles.statValue}>{produtosEstoqueBaixo.length}</span>
            <span className={styles.statLabel}>Estoque Baixo</span>
          </div>
        </div>

        <div className={`${styles.statCard} ${produtosExpirados.length > 0 ? styles.danger : ''}`}>
          <div className={styles.statIcon}>
            <FaExclamationTriangle />
          </div>
          <div className={styles.statContent}>
            <span className={styles.statValue}>{produtosExpirados.length}</span>
            <span className={styles.statLabel}>Expirados</span>
          </div>
        </div>
      </div>

      {/* Produtos por Categoria */}
      <section className={styles.section}>
        <h3 className={styles.sectionTitle}>Produtos por Categoria</h3>
        <div className={styles.categoryGrid}>
          {Object.values(CategoriaProduto).map((categoria) => (
            <div key={categoria} className={styles.categoryCard}>
              <span className={styles.categoryName}>{categoriaDescricao[categoria]}</span>
              <span className={styles.categoryCount}>{produtosPorCategoria[categoria] || 0}</span>
            </div>
          ))}
        </div>
      </section>

      {/* Alertas de Estoque Baixo */}
      {produtosEstoqueBaixo.length > 0 && (
        <section className={styles.section}>
          <h3 className={styles.sectionTitle}>
            <FaClipboardList className={styles.sectionIcon} />
            Produtos com Estoque Baixo
          </h3>
          <p className={styles.sectionDescription}>
            Estes produtos estão acabando. Clique para adicioná-los à lista de compras.
          </p>
          <div className={styles.alertList}>
            {produtosEstoqueBaixo.map((produto) => (
              <AlertCard
                key={produto.id}
                produto={produto}
                alertType="estoque-baixo"
                onAdicionarLista={handleAdicionarLista}
              />
            ))}
          </div>
        </section>
      )}

      {/* Alertas de Produtos Expirados */}
      {produtosExpirados.length > 0 && (
        <section className={styles.section}>
          <h3 className={styles.sectionTitle}>
            <FaExclamationTriangle className={styles.sectionIcon} />
            Produtos Expirados
          </h3>
          <p className={styles.sectionDescription}>
            Atenção! Estes produtos passaram da validade e devem ser descartados.
          </p>
          <div className={styles.alertList}>
            {produtosExpirados.map((produto) => (
              <AlertCard
                key={produto.id}
                produto={produto}
                alertType="expirado"
                onAdicionarLista={handleAdicionarLista}
              />
            ))}
          </div>
        </section>
      )}

      {/* Mensagem quando tudo está ok */}
      {produtosEstoqueBaixo.length === 0 && produtosExpirados.length === 0 && (
        <section className={styles.section}>
          <div className={styles.allGood}>
            <span className={styles.allGoodIcon}>✅</span>
            <h3>Tudo em ordem!</h3>
            <p>Sua despensa está organizada. Nenhum produto com estoque baixo ou expirado.</p>
          </div>
        </section>
      )}
    </div>
  );
};
