# Despensa Inteligente - Frontend

Frontend da aplicação Despensa Inteligente, desenvolvido em React, TypeScript e Vite seguindo o padrão **Atomic Design**.

## Tecnologias

- **React 19** - Biblioteca para construção de interfaces
- **TypeScript** - Tipagem estática
- **Vite** - Build tool rápida
- **React Router DOM** - Roteamento
- **Axios** - Cliente HTTP
- **Module Federation** - Micro-frontends

## Estrutura do Projeto (Atomic Design)

```
src/
├── components/
│   ├── atoms/       # Componentes básicos (Button, Input, Badge, Loading)
│   ├── molecules/   # Combinações de átomos (Card, Modal, AlertCard)
│   ├── organisms/   # Componentes complexos (Header, DataTable, Forms)
│   └── templates/   # Layouts de página (MainLayout)
├── pages/           # Páginas da aplicação
├── services/        # Serviços de API
├── types/           # Tipos TypeScript
└── hooks/           # Custom hooks
```

## Instalação e Execução

```bash
# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview

# Lint
npm run lint
```

## Module Federation

O projeto está configurado com **Module Federation** para permitir integração com um super app.

### Componentes Expostos

- `./App` - Aplicação completa
- `./Dashboard` - Página do Dashboard
- `./Products` - Página de Produtos
- `./Pantry` - Página da Despensa
- `./ShoppingList` - Página da Lista de Compras

### Exemplo de uso no Super App (Host)

```javascript
// vite.config.ts do host
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
  plugins: [
    federation({
      name: 'host',
      remotes: {
        despensa: 'http://localhost:3001/assets/remoteEntry.js',
      },
      shared: ['react', 'react-dom', 'react-router-dom'],
    }),
  ],
});

// No código React do host
const DespensaApp = React.lazy(() => import('despensa/App'));
const Dashboard = React.lazy(() => import('despensa/Dashboard'));
```

## Páginas

- **Dashboard** (`/`) - Visão geral com alertas de itens vencendo, vencidos e com estoque baixo
- **Produtos** (`/products`) - CRUD de produtos
- **Despensa** (`/pantry`) - Gerenciamento de itens na despensa
- **Lista de Compras** (`/shopping-list`) - Gerenciamento da lista de compras

## Design

O design segue um estilo **simplista** com:
- Cor principal: **Branco**
- Cor de destaque: **Azul** (#2563eb)
- Bordas, botões e texto da logo em azul

## Variáveis de Ambiente

```bash
# URL da API do backend (opcional, padrão: http://localhost:8080/api)
VITE_API_URL=http://localhost:8080/api
```

## Scripts

- `npm run dev` - Inicia o servidor de desenvolvimento na porta 3001
- `npm run build` - Compila para produção
- `npm run preview` - Preview da build de produção
- `npm run lint` - Executa o ESLint
