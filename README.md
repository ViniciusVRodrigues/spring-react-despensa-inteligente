# 🍳 Smart Pantry - Despensa Inteligente

Sistema de gerenciamento inteligente de despensa que controla produtos, validade, quantidade e gera recomendações automáticas de lista de compras.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Instalação e Execução](#instalação-e-execução)
- [API Endpoints](#api-endpoints)
- [Regras de Negócio](#regras-de-negócio)
- [Testes](#testes)

## 📖 Sobre o Projeto

O Smart Pantry é uma aplicação fullstack que permite o gerenciamento completo de uma despensa doméstica. Com ele você pode:

- Cadastrar produtos com quantidade, categoria e data de validade
- Controlar o consumo de produtos
- Visualizar produtos expirados e com estoque baixo
- Gerar automaticamente listas de compras baseadas no histórico de consumo
- Manter histórico de produtos (soft delete)

O sistema foi desenvolvido 100% em português brasileiro, tanto no frontend quanto no backend.

## ✨ Funcionalidades

### Gestão de Produtos
- ✅ Cadastro individual e em lote de produtos
- ✅ Atualização de informações do produto
- ✅ Consumo individual e em lote
- ✅ Descarte de produto (soft delete com histórico)
- ✅ Visualização de produtos expirados
- ✅ Visualização de produtos com estoque baixo

### Lista de Compras
- ✅ Adição manual de itens
- ✅ Remoção de itens
- ✅ Geração automática baseada em estoque baixo
- ✅ Marcação de itens como comprados

### Categorias de Produtos
- 🌾 Grãos
- 🥛 Laticínios
- 🥤 Bebidas
- 🥫 Enlatados
- ❄️ Congelados
- 📦 Outros

## 🏗️ Arquitetura

### Backend - Clean Architecture

O backend segue rigorosamente os princípios da **Clean Architecture**, organizando o código em camadas bem definidas:

```
┌──────────────────────────────────────────────────────────┐
│                    Infrastructure                        │
│  (Controllers, JPA Repositories, Configurations)         │
├──────────────────────────────────────────────────────────┤
│                      Application                         │
│  (Use Cases, Gateways/Ports, DTOs)                      │
├──────────────────────────────────────────────────────────┤
│                        Domain                            │
│  (Entities, Value Objects, Domain Exceptions)           │
└──────────────────────────────────────────────────────────┘
```

**Princípios seguidos:**
- **Dependency Rule**: Camadas internas nunca dependem de camadas externas
- **Use Cases**: Cada caso de uso representa uma única responsabilidade
- **Gateways (Ports)**: Interfaces que definem contratos para serviços externos
- **Adapters**: Implementações concretas dos gateways na camada de infraestrutura

### Frontend - Atomic Design

O frontend utiliza o padrão **Atomic Design** para organização dos componentes:

```
components/
├── atoms/       # Componentes básicos (Button, Input, Label)
├── molecules/   # Combinação de atoms (FormField, Card)
├── organisms/   # Seções completas (ProductList, ShoppingCart)
├── templates/   # Layouts de página
└── pages/       # Páginas da aplicação
```

## 🛠️ Tecnologias

### Backend
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 21 | Linguagem de programação |
| Spring Boot | 4.0.2 | Framework principal |
| Spring Data JPA | - | Persistência de dados |
| H2 Database | - | Banco de dados em memória |
| Gradle | 9.x | Build tool |
| Springdoc OpenAPI | 2.8.4 | Documentação da API |
| Lombok | - | Redução de boilerplate |
| JUnit 5 | - | Framework de testes |
| Mockito | - | Framework de mocks |

### Frontend
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| React | 19.2.0 | Biblioteca UI |
| TypeScript | 5.9.3 | Tipagem estática |
| Vite | 7.x | Build tool |
| Axios | 1.12.0 | Cliente HTTP |
| React Hook Form | 7.54.0 | Gerenciamento de formulários |
| date-fns | 3.6.0 | Manipulação de datas |
| react-icons | 5.4.0 | Biblioteca de ícones |
| react-hot-toast | 2.4.1 | Notificações |
| Vitest | 2.1.9 | Framework de testes |
| Testing Library | 16.x | Testes de componentes |

## 📁 Estrutura do Projeto

```
spring-react-despensa-inteligente/
├── edespensa/                          # Backend (Spring Boot)
│   ├── src/main/java/com/viniciusvr/edespensa/
│   │   ├── application/                # Camada de Aplicação
│   │   │   ├── controller/             # Controllers REST
│   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── gateway/                # Interfaces (Ports)
│   │   │   └── usecase/                # Casos de Uso
│   │   ├── domain/                     # Camada de Domínio
│   │   │   ├── entity/                 # Entidades de domínio
│   │   │   └── exception/              # Exceções de domínio
│   │   └── infrastructure/             # Camada de Infraestrutura
│   │       ├── config/                 # Configurações (CORS, OpenAPI)
│   │       ├── mapper/                 # Mapeadores DTO <-> Entity
│   │       └── persistence/            # Implementações JPA (Adapters)
│   └── src/test/java/                  # Testes
├── frontend/                           # Frontend (React)
│   ├── src/
│   │   ├── components/                 # Componentes (Atomic Design)
│   │   │   ├── atoms/
│   │   │   ├── molecules/
│   │   │   ├── organisms/
│   │   │   ├── templates/
│   │   │   └── pages/
│   │   ├── services/                   # Serviços de API
│   │   ├── types/                      # Tipos TypeScript
│   │   └── test/                       # Testes
│   └── package.json
└── README.md
```

## 🚀 Instalação e Execução

### Pré-requisitos

- Java 21+
- Node.js 18+
- npm ou yarn

### Backend

```bash
# Navegue até a pasta do backend
cd edespensa

# Execute a aplicação
./gradlew bootRun

# A API estará disponível em http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# Console H2: http://localhost:8080/h2-console
```

### Frontend

```bash
# Navegue até a pasta do frontend
cd frontend

# Instale as dependências
npm install

# Execute em modo de desenvolvimento
npm run dev

# A aplicação estará disponível em http://localhost:5173
```

### Build para Produção

```bash
# Backend
cd edespensa
./gradlew build

# Frontend
cd frontend
npm run build
```

## 📡 API Endpoints

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/products` | Criar produto (ação de compra) |
| `GET` | `/api/products` | Listar produtos da despensa |
| `GET` | `/api/products/{id}` | Buscar produto específico |
| `PUT` | `/api/products/{id}` | Atualizar produto |
| `DELETE` | `/api/products/{id}` | Descartar produto (soft delete) |
| `POST` | `/api/products/consume` | Consumir produtos (batch) |
| `POST` | `/api/products/batch` | Cadastrar produtos em lote |
| `GET` | `/api/products/expired` | Listar produtos expirados |
| `GET` | `/api/products/low-stock` | Listar produtos com estoque baixo |

### Lista de Compras

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/shopping-list` | Listar itens da lista |
| `POST` | `/api/shopping-list` | Adicionar item |
| `DELETE` | `/api/shopping-list/{id}` | Remover item |
| `POST` | `/api/shopping-list/auto-generate` | Gerar lista automática |

## 📋 Regras de Negócio

1. **Produtos sem data de validade**: O campo `dataValidade` pode ser `null`
2. **Consumo em lote**: Valida disponibilidade de todos os produtos antes de processar
3. **Lista de compras automática**: Inclui produtos com quantidade < 20% do estoque médio
4. **Produtos expirados**: Aparecem com destaque visual no frontend
5. **Descarte de produto**: Mantém histórico através de soft delete (campo `ativo`)
6. **Quantidade sugerida**: Na geração automática, sugere a diferença para atingir a média do estoque

## 🧪 Testes

### Backend

```bash
cd edespensa

# Executar todos os testes
./gradlew test

# Os relatórios ficam em build/reports/tests/test/index.html
```

### Frontend

```bash
cd frontend

# Executar testes
npm run test

# Executar testes em modo watch
npm run test:watch
```

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

Desenvolvido por Vinícius V. Rodrigues