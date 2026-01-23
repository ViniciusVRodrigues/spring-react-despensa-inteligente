import React from 'react';
import './Loading.css';

interface LoadingProps {
  size?: 'small' | 'medium' | 'large';
  text?: string;
}

export const Loading: React.FC<LoadingProps> = ({ size = 'medium', text }) => {
  return (
    <div className="loading-container">
      <div className={`loading-spinner loading-${size}`} />
      {text && <span className="loading-text">{text}</span>}
    </div>
  );
};
