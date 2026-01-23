import React from 'react';
import { Badge } from '../../atoms';
import './AlertCard.css';

interface AlertCardProps {
  title: string;
  subtitle?: string;
  badge?: {
    text: string;
    variant: 'default' | 'success' | 'warning' | 'danger' | 'info';
  };
  actions?: React.ReactNode;
  onClick?: () => void;
}

export const AlertCard: React.FC<AlertCardProps> = ({
  title,
  subtitle,
  badge,
  actions,
  onClick,
}) => {
  return (
    <div className={`alert-card ${onClick ? 'clickable' : ''}`} onClick={onClick}>
      <div className="alert-card-content">
        <div className="alert-card-info">
          <h4 className="alert-card-title">{title}</h4>
          {subtitle && <p className="alert-card-subtitle">{subtitle}</p>}
        </div>
        {badge && (
          <Badge variant={badge.variant} size="small">
            {badge.text}
          </Badge>
        )}
      </div>
      {actions && <div className="alert-card-actions">{actions}</div>}
    </div>
  );
};
