import React, { useId } from 'react';
import styles from './Input.module.css';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  fullWidth?: boolean;
}

/**
 * Componente de input reutilizável com suporte a label e erro.
 */
export const Input: React.FC<InputProps> = ({
  label,
  error,
  fullWidth = false,
  className,
  id,
  ...props
}) => {
  const generatedId = useId();
  const inputId = id || generatedId;
  
  const inputClasses = [
    styles.input,
    error ? styles.inputError : '',
    fullWidth ? styles.fullWidth : '',
    className || '',
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={`${styles.container} ${fullWidth ? styles.fullWidth : ''}`}>
      {label && (
        <label htmlFor={inputId} className={styles.label}>
          {label}
        </label>
      )}
      <input
        id={inputId}
        className={inputClasses}
        {...props}
      />
      {error && <span className={styles.error}>{error}</span>}
    </div>
  );
};
