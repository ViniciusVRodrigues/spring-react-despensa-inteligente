import React from 'react';
import { Header } from '../../organisms/Header';
import './MainLayout.css';

interface MainLayoutProps {
  children: React.ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div className="main-layout">
      <Header />
      <main className="main-content">
        <div className="content-container">{children}</div>
      </main>
    </div>
  );
};
