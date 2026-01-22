import React from 'react';
import { FaShoppingCart, FaTachometerAlt, FaBox } from 'react-icons/fa';
import styles from './Header.module.css';

type Tab = 'dashboard' | 'despensa' | 'lista-compras';

interface HeaderProps {
  activeTab: Tab;
  onTabChange: (tab: Tab) => void;
}

/**
 * Cabeçalho da aplicação com navegação por abas.
 */
export const Header: React.FC<HeaderProps> = ({
  activeTab,
  onTabChange,
}) => {
  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <div className={styles.brand}>
          <span className={styles.logo}>🏠</span>
          <h1 className={styles.title}>Smart Pantry</h1>
          <span className={styles.subtitle}>Despensa Inteligente</span>
        </div>
        
        <nav className={styles.nav}>
          <button
            className={`${styles.navButton} ${activeTab === 'dashboard' ? styles.active : ''}`}
            onClick={() => onTabChange('dashboard')}
          >
            <FaTachometerAlt />
            <span>Dashboard</span>
          </button>
          <button
            className={`${styles.navButton} ${activeTab === 'despensa' ? styles.active : ''}`}
            onClick={() => onTabChange('despensa')}
          >
            <FaBox />
            <span>Despensa</span>
          </button>
          <button
            className={`${styles.navButton} ${activeTab === 'lista-compras' ? styles.active : ''}`}
            onClick={() => onTabChange('lista-compras')}
          >
            <FaShoppingCart />
            <span>Lista de Compras</span>
          </button>
        </nav>
      </div>
    </header>
  );
};
