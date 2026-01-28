import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { routes } from '../../../routes';
import './Header.css';

export const Header: React.FC = () => {
  const location = useLocation();

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="header-logo">
          <span className="logo-icon">🏠</span>
          <span className="logo-text">Despensa Inteligente</span>
        </Link>
        <nav className="header-nav">
          {routes.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`nav-link ${location.pathname === item.path ? 'active' : ''}`}
            >
              {item.icon && <span className="nav-icon">{item.icon}</span>}
              {item.label}
            </Link>
          ))}
        </nav>
      </div>
    </header>
  );
};
