import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from './Button';

describe('Button', () => {
  it('deve renderizar o texto do botão', () => {
    render(<Button>Clique aqui</Button>);
    expect(screen.getByText('Clique aqui')).toBeInTheDocument();
  });

  it('deve executar o onClick quando clicado', () => {
    const handleClick = vi.fn();
    render(<Button onClick={handleClick}>Clique</Button>);
    
    fireEvent.click(screen.getByText('Clique'));
    
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('não deve executar onClick quando desabilitado', () => {
    const handleClick = vi.fn();
    render(<Button onClick={handleClick} disabled>Clique</Button>);
    
    fireEvent.click(screen.getByText('Clique'));
    
    expect(handleClick).not.toHaveBeenCalled();
  });

  it('deve mostrar o spinner quando loading=true', () => {
    render(<Button loading>Carregando</Button>);
    
    expect(screen.getByLabelText('Carregando')).toBeInTheDocument();
  });

  it('deve renderizar como botão HTML válido', () => {
    render(<Button variant="danger">Perigo</Button>);
    
    const button = screen.getByRole('button', { name: 'Perigo' });
    expect(button).toBeInTheDocument();
  });

  it('deve desabilitar o botão quando loading', () => {
    render(<Button loading>Carregando</Button>);
    
    const button = screen.getByRole('button');
    expect(button).toBeDisabled();
  });
});

