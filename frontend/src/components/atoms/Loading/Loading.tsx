import React from 'react';
import styles from './Loading.module.css';

interface LoadingProps {
  size?: 'small' | 'medium' | 'large';
  text?: string;
}

/**
 * Componente de loading com animação de spinner.
 */
export const Loading: React.FC<LoadingProps> = ({
  size = 'medium',
  text = 'Carregando...',
}) => {
  return (
    <div className={styles.container}>
      <div className={`${styles.spinner} ${styles[size]}`}></div>
      {text && <span className={styles.text}>{text}</span>}
    </div>
  );
};
