import React from 'react';
import styles from './Badge.module.css';

export type BadgeVariant = 'default' | 'success' | 'warning' | 'danger' | 'info';

interface BadgeProps {
  variant?: BadgeVariant;
  children: React.ReactNode;
  className?: string;
}

/**
 * Componente de badge para exibir status ou categorias.
 */
export const Badge: React.FC<BadgeProps> = ({
  variant = 'default',
  children,
  className,
}) => {
  const badgeClasses = [
    styles.badge,
    styles[variant],
    className || '',
  ]
    .filter(Boolean)
    .join(' ');

  return <span className={badgeClasses}>{children}</span>;
};
