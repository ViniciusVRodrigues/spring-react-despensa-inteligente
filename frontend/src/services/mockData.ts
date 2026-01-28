import type {
  Product,
  PantryItem,
  ShoppingListItem,
  DashboardAlerts,
  AlertItem,
} from '../types';

// Mock Products
export const mockProducts: Product[] = [
  {
    id: 1,
    name: 'Arroz',
    category: 'Grãos',
    unit: 'kg',
    description: 'Arroz branco tipo 1',
    trackExpiration: false,
  },
  {
    id: 2,
    name: 'Feijão',
    category: 'Grãos',
    unit: 'kg',
    description: 'Feijão preto',
    trackExpiration: false,
  },
  {
    id: 3,
    name: 'Leite',
    category: 'Laticínios',
    unit: 'L',
    description: 'Leite integral',
    trackExpiration: true,
  },
  {
    id: 4,
    name: 'Manteiga',
    category: 'Laticínios',
    unit: 'g',
    description: 'Manteiga com sal',
    trackExpiration: true,
  },
  {
    id: 5,
    name: 'Açúcar',
    category: 'Condimentos',
    unit: 'kg',
    description: 'Açúcar refinado',
    trackExpiration: false,
  },
  {
    id: 6,
    name: 'Café',
    category: 'Bebidas',
    unit: 'g',
    description: 'Café torrado e moído',
    trackExpiration: false,
  },
  {
    id: 7,
    name: 'Ovos',
    category: 'Proteínas',
    unit: 'dúzia',
    description: 'Ovos brancos',
    trackExpiration: true,
  },
  {
    id: 8,
    name: 'Queijo',
    category: 'Laticínios',
    unit: 'g',
    description: 'Queijo mussarela',
    trackExpiration: true,
  },
];

// Helper function to generate dates
const addDays = (date: Date, days: number): string => {
  const result = new Date(date);
  result.setDate(result.getDate() + days);
  return result.toISOString().split('T')[0];
};

const today = new Date();

// Mock Pantry Items
export const mockPantryItems: PantryItem[] = [
  {
    id: 1,
    product: mockProducts[0], // Arroz
    quantity: 5,
    expirationDate: null,
    addedDate: addDays(today, -10),
    location: 'Armário da cozinha',
    notes: '',
    isExpired: false,
    isExpiringSoon: false,
    isLowStock: false,
  },
  {
    id: 2,
    product: mockProducts[1], // Feijão
    quantity: 2,
    expirationDate: null,
    addedDate: addDays(today, -5),
    location: 'Armário da cozinha',
    notes: '',
    isExpired: false,
    isExpiringSoon: false,
    isLowStock: true,
  },
  {
    id: 3,
    product: mockProducts[2], // Leite
    quantity: 1,
    expirationDate: addDays(today, 3),
    addedDate: addDays(today, -2),
    location: 'Geladeira',
    notes: '',
    isExpired: false,
    isExpiringSoon: true,
    isLowStock: false,
  },
  {
    id: 4,
    product: mockProducts[3], // Manteiga
    quantity: 1,
    expirationDate: addDays(today, -2),
    addedDate: addDays(today, -30),
    location: 'Geladeira',
    notes: '',
    isExpired: true,
    isExpiringSoon: false,
    isLowStock: false,
  },
  {
    id: 5,
    product: mockProducts[6], // Ovos
    quantity: 1,
    expirationDate: addDays(today, 5),
    addedDate: addDays(today, -1),
    location: 'Geladeira',
    notes: '',
    isExpired: false,
    isExpiringSoon: true,
    isLowStock: true,
  },
  {
    id: 6,
    product: mockProducts[7], // Queijo
    quantity: 2,
    expirationDate: addDays(today, 10),
    addedDate: addDays(today, -3),
    location: 'Geladeira',
    notes: '',
    isExpired: false,
    isExpiringSoon: false,
    isLowStock: false,
  },
];

// Mock Shopping List Items
export const mockShoppingListItems: ShoppingListItem[] = [
  {
    id: 1,
    product: mockProducts[1], // Feijão
    quantity: 2,
    priority: 'HIGH',
    status: 'PENDING',
    addedAt: addDays(today, -1),
    notes: 'Comprar no mercado',
    autoAdded: true,
  },
  {
    id: 2,
    product: mockProducts[5], // Café
    quantity: 1,
    priority: 'MEDIUM',
    status: 'PENDING',
    addedAt: addDays(today, -2),
    notes: '',
    autoAdded: false,
  },
  {
    id: 3,
    product: mockProducts[6], // Ovos
    quantity: 2,
    priority: 'HIGH',
    status: 'PENDING',
    addedAt: addDays(today, 0),
    notes: '',
    autoAdded: true,
  },
];

// Generate Dashboard Alerts from Pantry Items
const generateAlertItems = (): DashboardAlerts => {
  const expiringSoon: AlertItem[] = [];
  const expired: AlertItem[] = [];
  const lowStock: AlertItem[] = [];

  mockPantryItems.forEach((item) => {
    const alertItem: AlertItem = {
      pantryItemId: item.id,
      productName: item.product.name,
      quantity: item.quantity,
      unit: item.product.unit,
      expirationDate: item.expirationDate,
      daysUntilExpiration: item.expirationDate
        ? Math.ceil((new Date(item.expirationDate).getTime() - today.getTime()) / (1000 * 60 * 60 * 24))
        : null,
      alertType: '',
    };

    if (item.isExpired) {
      expired.push({ ...alertItem, alertType: 'EXPIRED' });
    }
    if (item.isExpiringSoon && !item.isExpired) {
      expiringSoon.push({ ...alertItem, alertType: 'EXPIRING_SOON' });
    }
    if (item.isLowStock) {
      lowStock.push({ ...alertItem, alertType: 'LOW_STOCK' });
    }
  });

  return {
    expiringSoon,
    expired,
    lowStock,
    totalAlerts: expiringSoon.length + expired.length + lowStock.length,
    summary: {
      expiringCount: expiringSoon.length,
      expiredCount: expired.length,
      lowStockCount: lowStock.length,
    },
  };
};

export const mockDashboardAlerts = generateAlertItems();
