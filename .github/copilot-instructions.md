# Smart Pantry - Copilot Instructions

## Project Overview

Sistema de gerenciamento inteligente de despensa que controla produtos, validade, quantidade e gera recomendações automáticas de lista de compras.

O sistema deve ser 100% em português brasileiro, tanto no frontend quanto no backend, incluindo nomes de variáveis, comentários e mensagens para o usuário. Somente termos técnicos universalmente reconhecidos podem ser mantidos em inglês.

## Tech Stack

### Frontend
- **Framework**: React 18+ com TypeScript
- **Build Tool**: Vite
- **Architecture**: Atomic Design Pattern
- **Module Federation**: Configurado com @originjs/vite-plugin-federation
- **State Management**: Context API ou Zustand
- **Styling**: CSS Modules ou Styled Components
- **HTTP Client**: Axios ou Fetch API

### Backend
- **Framework**: Spring Boot 4.0.2
- **Language**: Java 21
- **Architecture**: Clean Architecture
- **Database**: H2 Database (development)
- **ORM**: Spring Data JPA
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Gradle - Groovy

## Project Structure

### Frontend Structure (Atomic Design)

### Backend Structure (Clean Architecture)

## Coding Guidelines

### TypeScript/React Guidelines
- **Always use TypeScript** com strict mode habilitado
- **Functional Components** com hooks (sem class components)
- **Named exports** preferencialmente
- **Props Interface**: Sempre tipar props com interfaces
- **Async/Await**: Preferir sobre promises chains
- **Error Handling**: Implementar try-catch em chamadas assíncronas
- **Semicolons**: Sempre usar
- **ESLint**: Seguir configuração do projeto

```typescript
// ✅ Exemplo correto
interface ProductCardProps {
  id: string;
  name: string;
  quantity: number;
  expiryDate?: Date;
  onConsume: (id: string, amount: number) => Promise<void>;
}

export const ProductCard: React.FC<ProductCardProps> = ({ 
  id, 
  name, 
  quantity, 
  expiryDate,
  onConsume 
}) => {
  const [isLoading, setIsLoading] = useState(false);
  
  const handleConsume = async (amount: number) => {
    try {
      setIsLoading(true);
      await onConsume(id, amount);
    } catch (error) {
      console.error('Erro ao consumir produto:', error);
    } finally {
      setIsLoading(false);
    }
  };
  
  return (/* JSX */);
};
```

### Java/Spring Boot Guidelines
- **Clean Architecture**: Respeitar rigorosamente as camadas
- **Dependency Rule**: Camadas internas nunca dependem de externas
- **Use Cases**: Um caso de uso = uma responsabilidade
- **DTOs**: Usar para comunicação entre camadas
- **Validation**: Bean Validation nas DTOs
- **Exception Handling**: @ControllerAdvice para tratamento global
- **Naming**: Usar nomenclatura descritiva (UseCase suffix para casos de uso)

```java
// ✅ Exemplo correto - Use Case
@Service
public class ConsumeProductUseCase {
    private final ProductGateway productGateway;
    
    public ConsumeProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }
    
    public void execute(String productId, Integer quantity) {
        Product product = productGateway.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
            
        product.consume(quantity);
        productGateway.save(product);
    }
}
```

## Domain Models

### Product Entity
```java
// Domain Layer
@Entity
public class Product {
    private String id;
    private String name;
    private Integer quantity;
    private LocalDate expiryDate;
    private ProductCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business logic methods
    public void consume(Integer amount);
    public void restock(Integer amount);
    public boolean isExpired();
    public boolean isRunningLow(Integer threshold);
}
```

### TypeScript Types
```typescript
// Frontend types
interface Product {
  id: string;
  name: string;
  quantity: number;
  expiryDate?: string; // ISO 8601
  category: ProductCategory;
  createdAt: string;
  updatedAt: string;
}

enum ProductCategory {
  GRAINS = 'GRAINS',
  DAIRY = 'DAIRY',
  BEVERAGES = 'BEVERAGES',
  CANNED = 'CANNED',
  FROZEN = 'FROZEN',
  OTHER = 'OTHER'
}
```

## API Endpoints Structure

Todos os endpoints devem seguir REST conventions:

```
POST   /api/products                    # Criar produto (ação de compra)
GET    /api/products                    # Listar produtos da despensa
GET    /api/products/{id}               # Buscar produto específico
PUT    /api/products/{id}               # Atualizar produto
DELETE /api/products/{id}               # Deletar produto (descarte)

POST   /api/products/consume            # Ação de consumo (batch)
POST   /api/products/batch              # Cadastro em lote

GET    /api/shopping-list               # Listar itens da lista
POST   /api/shopping-list               # Adicionar item
DELETE /api/shopping-list/{id}          # Remover item
POST   /api/shopping-list/auto-generate # Gerar lista baseado em produtos baixos
```

## Module Federation Configuration

```typescript
// vite.config.ts
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: 'smart_pantry',
      filename: 'remoteEntry.js',
      exposes: {
        './ProductManager': './src/components/organisms/ProductManager',
        './ShoppingList': './src/components/organisms/ShoppingList',
      },
      shared: ['react', 'react-dom'],
    }),
  ],
  build: {
    modulePreload: false,
    target: 'esnext',
    minify: false,
    cssCodeSplit: false,
  },
});
```

## Testing Requirements

### Frontend
- **Unit Tests**: Jest + React Testing Library
- **Coverage**: Mínimo 70% para componentes organisms e pages
- **Test Files**: `*.test.tsx` ou `*.spec.tsx`
- **Mock API**: MSW (Mock Service Worker) para testes de integração

### Backend
- **Unit Tests**: JUnit 5 + Mockito
- **Integration Tests**: @SpringBootTest para controllers
- **Coverage**: Mínimo 80% para use cases
- **Test Naming**: `should[ExpectedBehavior]When[Condition]`

```java
@Test
void shouldConsumeProductWhenQuantityIsAvailable() {
    // Arrange
    Product product = new Product("1", "Arroz", 10);
    
    // Act
    product.consume(5);
    
    // Assert
    assertEquals(5, product.getQuantity());
}
```

## Security Considerations

- **Input Validation**: Validar todas as entradas do usuário
- **SQL Injection**: Usar apenas JPA queries parametrizadas
- **XSS Protection**: Sanitizar dados antes de renderizar
- **CORS**: Configurar adequadamente no Spring Boot

## Performance Guidelines

- **Lazy Loading**: Implementar para componentes pesados
- **Pagination**: Listar produtos com paginação (backend)
- **Debounce**: Usar em campos de busca
- **Memoization**: React.memo para componentes que re-renderizam frequentemente

## Git Workflow

- **Branch Naming**: `feature/nome-da-feature`, `fix/nome-do-bug`
- **Commits**: Conventional Commits (feat:, fix:, docs:, refactor:)
- **PR**: Sempre criar PR com descrição detalhada

## Resources and Scripts

- `npm run dev` - Inicia frontend em modo desenvolvimento
- `npm run build` - Build de produção
- `npm run test` - Executa testes frontend
- `./mvnw spring-boot:run` - Inicia backend
- `./mvnw test` - Executa testes backend

## Additional Notes

- **Date Handling**: Usar date-fns ou dayjs no frontend
- **Icons**: Usar biblioteca como react-icons
- **Forms**: React Hook Form para gerenciamento de formulários
- **Toast/Notifications**: Implementar feedback visual para ações
- **Swagger**: Acessível em `/swagger-ui/index.html` quando backend estiver rodando

## Business Rules

1. Produtos sem data de validade devem ter o campo `expiryDate` como `null`
2. Consumo em lote deve validar disponibilidade de todos produtos antes de processar
3. Lista de compras auto-gerada deve incluir produtos com quantidade < 20% do estoque médio
4. Produtos expirados devem aparecer com destaque visual
5. Descarte de produto deve manter histórico (soft delete)
