import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Header.css';

export const Header: React.FC = () => {
  const location = useLocation();

  const resolvedPath = 'edespensa';

  const navItems = [
    { path: '/', label: 'Dashboard' },
    { path: '/products', label: 'Produtos' },
    { path: '/pantry', label: 'Despensa' },
    { path: '/shopping-list', label: 'Lista de Compras' },
  ];

  


  return (
    <header className="header">
      <div className="header-container">
        <Link to={`/${resolvedPath}/`} className="header-logo">
          <span className="logo-icon">🏠</span>
          <span className="logo-text">Despensa Inteligente</span>
        </Link>
        <nav className="header-nav">
          {navItems.map((item) => (
            <Link
              key={item.path}
              to={`/${resolvedPath}${item.path}`}
              className={`nav-link ${location.pathname === `/${resolvedPath}${item.path}` ? 'active' : ''}`}
            >
              {item.label}
            </Link>
          ))}
        </nav>
      </div>
    </header>
  );
};
