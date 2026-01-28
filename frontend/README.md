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
│   ├── api.ts       # API real
│   ├── mockApi.ts   # API mockada
│   └── mockData.ts  # Dados mockados
├── types/           # Tipos TypeScript
├── routes.ts        # Configuração de rotas (exposto via Module Federation)
└── hooks/           # Custom hooks
```

## Instalação e Execução

```bash
# Instalar dependências
npm install

# Executar em modo de desenvolvimento (com API backend)
npm run dev

# Executar com dados mockados (sem API backend)
VITE_USE_MOCK_DATA=true npm run dev

# Build para produção
npm run build

# Build para GitHub Pages (com dados mockados)
VITE_USE_MOCK_DATA=true VITE_BASE_PATH=/spring-react-despensa-inteligente/ npm run build

# Preview da build
npm run preview

# Lint
npm run lint
```

## Variáveis de Ambiente

Crie um arquivo `.env` baseado no `.env.example`:

```bash
# URL da API do backend
VITE_API_URL=http://localhost:8081/api

# Porta do servidor de desenvolvimento
VITE_PORT=3002

# Usar dados mockados ao invés da API real (true/false)
VITE_USE_MOCK_DATA=false

# Base path para deploy (ex: /spring-react-despensa-inteligente/ para GitHub Pages)
VITE_BASE_PATH=/
```

## Modo com Dados Mockados

Para permitir o deploy no **GitHub Pages** sem dependência de uma API backend, o projeto suporta um modo com dados mockados.

Quando `VITE_USE_MOCK_DATA=true`:
- Todos os dados são armazenados no `localStorage` do navegador
- Operações CRUD funcionam normalmente
- Os dados persistem entre reloads da página
- Inclui dados de exemplo:
  - 8 produtos (Arroz, Feijão, Leite, Manteiga, etc.)
  - 6 itens na despensa com diferentes status
  - 3 itens na lista de compras

## Module Federation

O projeto está configurado com **Module Federation** para permitir integração com um super app.

### Componentes Expostos

- `./App` - Aplicação completa
- `./Dashboard` - Página do Dashboard
- `./Products` - Página de Produtos
- `./Pantry` - Página da Despensa
- `./ShoppingList` - Página da Lista de Compras
- `./routes` - **Configuração de rotas** (RouteConfig[])

### Estrutura de Rotas

O arquivo `routes.ts` exporta um array de objetos `RouteConfig`:

```typescript
interface RouteConfig {
  path: string;        // Caminho da rota (ex: '/', '/products')
  label: string;       // Label para exibição no menu
  icon?: string;       // Emoji ou ícone para o menu
  component: string;   // Nome do componente exposto pelo Module Federation
}
```

### Exemplo de uso no Super App (Host)

```typescript
// vite.config.ts do host
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
  plugins: [
    federation({
      name: 'host',
      remotes: {
        despensa: 'http://localhost:3002/assets/remoteEntry.js',
      },
      shared: ['react', 'react-dom', 'react-router-dom'],
    }),
  ],
});

// No código React do host - Importar rotas
import routes from 'despensa/routes';

// Gerar menu dinamicamente
const menu = routes.map(route => (
  <Link key={route.path} to={route.path}>
    {route.icon} {route.label}
  </Link>
));

// Gerar rotas dinamicamente
const routeElements = routes.map(route => {
  const Component = lazy(() => import(`despensa/${route.component}`));
  return <Route key={route.path} path={route.path} element={<Component />} />;
});
```

## Deploy no GitHub Pages

O projeto está configurado para deploy automático no GitHub Pages através do GitHub Actions.

O workflow `.github/workflows/deploy-gh-pages.yml` é acionado automaticamente em push para a branch `main` e:
1. Instala as dependências
2. Faz o build com `VITE_USE_MOCK_DATA=true`
3. Faz deploy para GitHub Pages

Acesse a aplicação em: `https://viniciusvrodrigues.github.io/spring-react-despensa-inteligente/`

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

## Scripts

- `npm run dev` - Inicia o servidor de desenvolvimento na porta 3002
- `npm run build` - Compila para produção
- `npm run preview` - Preview da build de produção
- `npm run lint` - Executa o ESLint
