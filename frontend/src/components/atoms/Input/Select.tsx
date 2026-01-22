import React, { useId } from 'react';
import styles from './Select.module.css';

interface SelectOption {
  value: string;
  label: string;
}

interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  options: SelectOption[];
  placeholder?: string;
  fullWidth?: boolean;
}

/**
 * Componente de select reutilizável com suporte a label e erro.
 */
export const Select: React.FC<SelectProps> = ({
  label,
  error,
  options,
  placeholder,
  fullWidth = false,
  className,
  id,
  ...props
}) => {
  const generatedId = useId();
  const selectId = id || generatedId;
  
  const selectClasses = [
    styles.select,
    error ? styles.selectError : '',
    fullWidth ? styles.fullWidth : '',
    className || '',
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={`${styles.container} ${fullWidth ? styles.fullWidth : ''}`}>
      {label && (
        <label htmlFor={selectId} className={styles.label}>
          {label}
        </label>
      )}
      <select id={selectId} className={selectClasses} {...props}>
        {placeholder && (
          <option value="" disabled>
            {placeholder}
          </option>
        )}
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error && <span className={styles.error}>{error}</span>}
    </div>
  );
};
