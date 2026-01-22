import React from 'react';
import { Header } from '../../organisms/Header';
import styles from './MainLayout.module.css';

type Tab = 'dashboard' | 'despensa' | 'lista-compras';

interface MainLayoutProps {
  activeTab: Tab;
  onTabChange: (tab: Tab) => void;
  children: React.ReactNode;
}

/**
 * Template principal da aplicação com Header e área de conteúdo.
 */
export const MainLayout: React.FC<MainLayoutProps> = ({
  activeTab,
  onTabChange,
  children,
}) => {
  return (
    <div className={styles.layout}>
      <Header activeTab={activeTab} onTabChange={onTabChange} />
      <main className={styles.main}>
        {children}
      </main>
    </div>
  );
};
