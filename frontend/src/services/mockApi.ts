import type {
  Product,
  ProductRequest,
  PantryItem,
  PantryItemRequest,
  PantryItemUpdateRequest,
  QuickPurchaseRequest,
  ShoppingListItem,
  ShoppingListItemRequest,
  DashboardAlerts,
  ConsumeRequest,
  ConsumptionResponse,
} from '../types';
import {
  mockProducts,
  mockPantryItems,
  mockShoppingListItems,
} from './mockData';

// Local storage keys
const STORAGE_KEYS = {
  PRODUCTS: 'despensa_products',
  PANTRY: 'despensa_pantry',
  SHOPPING_LIST: 'despensa_shopping_list',
};

// Helper to get data from localStorage with fallback
const getStorageData = <T>(key: string, fallback: T): T => {
  try {
    const stored = localStorage.getItem(key);
    return stored ? JSON.parse(stored) : fallback;
  } catch {
    return fallback;
  }
};

// Helper to set data in localStorage
const setStorageData = <T>(key: string, data: T): void => {
  try {
    localStorage.setItem(key, JSON.stringify(data));
  } catch (error) {
    console.error('Error saving to localStorage:', error);
  }
};

// Initialize localStorage with mock data if not present
const initializeStorage = () => {
  if (!localStorage.getItem(STORAGE_KEYS.PRODUCTS)) {
    setStorageData(STORAGE_KEYS.PRODUCTS, mockProducts);
  }
  if (!localStorage.getItem(STORAGE_KEYS.PANTRY)) {
    setStorageData(STORAGE_KEYS.PANTRY, mockPantryItems);
  }
  if (!localStorage.getItem(STORAGE_KEYS.SHOPPING_LIST)) {
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
  }
};

// Initialize on module load
initializeStorage();

// Simulate async API calls with delay
const delay = (ms: number = 300) => new Promise((resolve) => setTimeout(resolve, ms));

// Mock API response wrapper
const mockResponse = async <T>(data: T): Promise<{ data: T }> => {
  await delay();
  return { data };
};

// Product API Mock
export const mockProductApi = {
  getAll: async () => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    return mockResponse(products);
  },

  getById: async (id: number) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const product = products.find((p) => p.id === id);
    if (!product) throw new Error('Product not found');
    return mockResponse(product);
  },

  getByCategory: async (category: string) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const filtered = products.filter((p) => p.category === category);
    return mockResponse(filtered);
  },

  search: async (q: string) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const filtered = products.filter((p) =>
      p.name.toLowerCase().includes(q.toLowerCase()) ||
      p.category.toLowerCase().includes(q.toLowerCase())
    );
    return mockResponse(filtered);
  },

  create: async (data: ProductRequest) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const newId = Math.max(...products.map((p) => p.id), 0) + 1;
    const newProduct: Product = {
      id: newId,
      description: data.description || '',
      ...data,
    };
    products.push(newProduct);
    setStorageData(STORAGE_KEYS.PRODUCTS, products);
    return mockResponse(newProduct);
  },

  update: async (id: number, data: ProductRequest) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const index = products.findIndex((p) => p.id === id);
    if (index === -1) throw new Error('Product not found');
    const updated = { ...products[index], ...data };
    products[index] = updated;
    setStorageData(STORAGE_KEYS.PRODUCTS, products);
    return mockResponse(updated);
  },

  delete: async (id: number) => {
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const filtered = products.filter((p) => p.id !== id);
    setStorageData(STORAGE_KEYS.PRODUCTS, filtered);
    return mockResponse({});
  },
};

// Helper to calculate pantry item flags
const calculatePantryFlags = (item: PantryItem): PantryItem => {
  const today = new Date();
  const expirationDate = item.expirationDate ? new Date(item.expirationDate) : null;
  
  const isExpired = expirationDate ? expirationDate < today : false;
  const daysUntilExpiration = expirationDate
    ? Math.ceil((expirationDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24))
    : null;
  const isExpiringSoon = daysUntilExpiration !== null && daysUntilExpiration <= 7 && daysUntilExpiration >= 0;
  const isLowStock = item.quantity <= 2;

  return {
    ...item,
    isExpired,
    isExpiringSoon,
    isLowStock,
  };
};

// Pantry API Mock
export const mockPantryApi = {
  getAll: async () => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const updated = pantryItems.map(calculatePantryFlags);
    return mockResponse(updated);
  },

  getById: async (id: number) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const item = pantryItems.find((p) => p.id === id);
    if (!item) throw new Error('Pantry item not found');
    return mockResponse(calculatePantryFlags(item));
  },

  getByProduct: async (productId: number) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const filtered = pantryItems.filter((p) => p.product.id === productId);
    return mockResponse(filtered.map(calculatePantryFlags));
  },

  search: async (q: string) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const filtered = pantryItems.filter((p) =>
      p.product.name.toLowerCase().includes(q.toLowerCase())
    );
    return mockResponse(filtered.map(calculatePantryFlags));
  },

  add: async (data: PantryItemRequest) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    const product = products.find((p) => p.id === data.productId);
    if (!product) throw new Error('Product not found');

    const newId = Math.max(...pantryItems.map((p) => p.id), 0) + 1;
    const newItem: PantryItem = {
      id: newId,
      product,
      quantity: data.quantity,
      expirationDate: data.expirationDate || null,
      addedDate: new Date().toISOString().split('T')[0],
      location: data.location || '',
      notes: data.notes || '',
      isExpired: false,
      isExpiringSoon: false,
      isLowStock: false,
    };
    
    pantryItems.push(newItem);
    setStorageData(STORAGE_KEYS.PANTRY, pantryItems);
    return mockResponse(calculatePantryFlags(newItem));
  },

  quickPurchase: async (data: QuickPurchaseRequest) => {
    // If productId is provided, use it; otherwise create a new product
    let product: Product;
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);

    if (data.productId) {
      const existingProduct = products.find((p) => p.id === data.productId);
      if (!existingProduct) throw new Error('Product not found');
      product = existingProduct;
    } else if (data.productName) {
      const newId = Math.max(...products.map((p) => p.id), 0) + 1;
      product = {
        id: newId,
        name: data.productName,
        category: data.category || 'Outros',
        unit: data.unit || 'un',
        description: '',
        trackExpiration: !!data.expirationDate,
      };
      products.push(product);
      setStorageData(STORAGE_KEYS.PRODUCTS, products);
    } else {
      throw new Error('Either productId or productName must be provided');
    }

    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const newId = Math.max(...pantryItems.map((p) => p.id), 0) + 1;
    const newItem: PantryItem = {
      id: newId,
      product,
      quantity: data.quantity,
      expirationDate: data.expirationDate || null,
      addedDate: new Date().toISOString().split('T')[0],
      location: data.location || '',
      notes: data.notes || '',
      isExpired: false,
      isExpiringSoon: false,
      isLowStock: false,
    };
    
    pantryItems.push(newItem);
    setStorageData(STORAGE_KEYS.PANTRY, pantryItems);
    return mockResponse(calculatePantryFlags(newItem));
  },

  update: async (id: number, data: PantryItemUpdateRequest) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const index = pantryItems.findIndex((p) => p.id === id);
    if (index === -1) throw new Error('Pantry item not found');
    
    const updated = { ...pantryItems[index], ...data };
    pantryItems[index] = updated;
    setStorageData(STORAGE_KEYS.PANTRY, pantryItems);
    return mockResponse(calculatePantryFlags(updated));
  },

  consume: async (id: number, data: ConsumeRequest) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const index = pantryItems.findIndex((p) => p.id === id);
    if (index === -1) throw new Error('Pantry item not found');

    const item = pantryItems[index];
    const newQuantity = item.quantity - data.quantity;
    
    const depletedItemIds: number[] = [];
    const depletedProductNames: string[] = [];
    
    if (newQuantity <= 0) {
      // Remove item if depleted
      pantryItems.splice(index, 1);
      depletedItemIds.push(id);
      depletedProductNames.push(item.product.name);
    } else {
      // Update quantity
      pantryItems[index] = { ...item, quantity: newQuantity };
    }
    
    setStorageData(STORAGE_KEYS.PANTRY, pantryItems);
    
    const response: ConsumptionResponse = {
      itemsConsumed: 1,
      depletedItemIds,
      depletedProductNames,
      message: depletedItemIds.length > 0 
        ? `Item ${depletedProductNames.join(', ')} foi consumido completamente`
        : 'Consumo registrado com sucesso',
    };
    
    return mockResponse(response);
  },

  discard: async (id: number) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const filtered = pantryItems.filter((p) => p.id !== id);
    setStorageData(STORAGE_KEYS.PANTRY, filtered);
    return mockResponse({});
  },

  discardExpired: async () => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const today = new Date();
    const filtered = pantryItems.filter((item) => {
      if (!item.expirationDate) return true;
      return new Date(item.expirationDate) >= today;
    });
    setStorageData(STORAGE_KEYS.PANTRY, filtered);
    return mockResponse({});
  },

  delete: async (id: number) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const filtered = pantryItems.filter((p) => p.id !== id);
    setStorageData(STORAGE_KEYS.PANTRY, filtered);
    return mockResponse({});
  },
};

// Shopping List API Mock
export const mockShoppingListApi = {
  getAll: async () => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    return mockResponse(items);
  },

  getPending: async () => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const pending = items.filter((item) => item.status === 'PENDING');
    return mockResponse(pending);
  },

  getById: async (id: number) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const item = items.find((i) => i.id === id);
    if (!item) throw new Error('Shopping list item not found');
    return mockResponse(item);
  },

  add: async (data: ShoppingListItemRequest) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const products = getStorageData<Product[]>(STORAGE_KEYS.PRODUCTS, mockProducts);
    
    let product: Product;
    if (data.productId) {
      const existingProduct = products.find((p) => p.id === data.productId);
      if (!existingProduct) throw new Error('Product not found');
      product = existingProduct;
    } else if (data.productName) {
      const newId = Math.max(...products.map((p) => p.id), 0) + 1;
      product = {
        id: newId,
        name: data.productName,
        category: 'Outros',
        unit: 'un',
        description: '',
        trackExpiration: false,
      };
      products.push(product);
      setStorageData(STORAGE_KEYS.PRODUCTS, products);
    } else {
      throw new Error('Either productId or productName must be provided');
    }

    const newId = Math.max(...items.map((i) => i.id), 0) + 1;
    const newItem: ShoppingListItem = {
      id: newId,
      product,
      quantity: data.quantity || 1,
      priority: (data.priority as any) || 'MEDIUM',
      status: 'PENDING',
      addedAt: new Date().toISOString(),
      notes: data.notes || '',
      autoAdded: false,
    };
    
    items.push(newItem);
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse(newItem);
  },

  update: async (id: number, data: Partial<ShoppingListItemRequest>) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const index = items.findIndex((i) => i.id === id);
    if (index === -1) throw new Error('Shopping list item not found');
    
    // Only update allowed fields
    const currentItem = items[index];
    const updated: ShoppingListItem = {
      ...currentItem,
      ...(data.quantity !== undefined && { quantity: data.quantity }),
      ...(data.priority !== undefined && { priority: data.priority as any }),
      ...(data.notes !== undefined && { notes: data.notes }),
    };
    items[index] = updated;
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse(updated);
  },

  markAsPurchased: async (id: number) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const index = items.findIndex((i) => i.id === id);
    if (index === -1) throw new Error('Shopping list item not found');
    
    items[index] = { ...items[index], status: 'PURCHASED' };
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse({});
  },

  markAsPurchasedBatch: async (ids: number[]) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    ids.forEach((id) => {
      const index = items.findIndex((i) => i.id === id);
      if (index !== -1) {
        items[index] = { ...items[index], status: 'PURCHASED' };
      }
    });
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse({});
  },

  cancel: async (id: number) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const index = items.findIndex((i) => i.id === id);
    if (index === -1) throw new Error('Shopping list item not found');
    
    items[index] = { ...items[index], status: 'CANCELLED' };
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse({});
  },

  delete: async (id: number) => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const filtered = items.filter((i) => i.id !== id);
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, filtered);
    return mockResponse({});
  },

  clearPurchased: async () => {
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    const filtered = items.filter((item) => item.status !== 'PURCHASED');
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, filtered);
    return mockResponse({});
  },
};

// Dashboard API Mock
export const mockDashboardApi = {
  getAlerts: async () => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const today = new Date();
    
    const expiringSoon: any[] = [];
    const expired: any[] = [];
    const lowStock: any[] = [];
    
    pantryItems.forEach((item) => {
      const updated = calculatePantryFlags(item);
      const expirationDate = updated.expirationDate ? new Date(updated.expirationDate) : null;
      const daysUntilExpiration = expirationDate
        ? Math.ceil((expirationDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24))
        : null;
      
      const alertItem = {
        pantryItemId: item.id,
        productName: item.product.name,
        quantity: item.quantity,
        unit: item.product.unit,
        expirationDate: item.expirationDate,
        daysUntilExpiration,
        alertType: '',
      };
      
      if (updated.isExpired) {
        expired.push({ ...alertItem, alertType: 'EXPIRED' });
      }
      if (updated.isExpiringSoon && !updated.isExpired) {
        expiringSoon.push({ ...alertItem, alertType: 'EXPIRING_SOON' });
      }
      if (updated.isLowStock) {
        lowStock.push({ ...alertItem, alertType: 'LOW_STOCK' });
      }
    });
    
    const alerts: DashboardAlerts = {
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
    
    return mockResponse(alerts);
  },

  addAllAlertsToShoppingList: async () => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    
    const alertItems = pantryItems.filter((item) => {
      const updated = calculatePantryFlags(item);
      return updated.isExpired || updated.isExpiringSoon || updated.isLowStock;
    });
    
    let nextId = Math.max(...items.map((i) => i.id), 0) + 1;
    
    alertItems.forEach((item) => {
      const newItem: ShoppingListItem = {
        id: nextId++,
        product: item.product,
        quantity: item.quantity,
        priority: 'MEDIUM',
        status: 'PENDING',
        addedAt: new Date().toISOString(),
        notes: 'Adicionado automaticamente dos alertas',
        autoAdded: true,
      };
      items.push(newItem);
    });
    
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse({});
  },

  addSelectedAlertsToShoppingList: async (data: { pantryItemIds: number[] }) => {
    const pantryItems = getStorageData<PantryItem[]>(STORAGE_KEYS.PANTRY, mockPantryItems);
    const items = getStorageData<ShoppingListItem[]>(STORAGE_KEYS.SHOPPING_LIST, mockShoppingListItems);
    
    const selectedItems = pantryItems.filter((item) => data.pantryItemIds.includes(item.id));
    let nextId = Math.max(...items.map((i) => i.id), 0) + 1;
    
    selectedItems.forEach((item) => {
      const newItem: ShoppingListItem = {
        id: nextId++,
        product: item.product,
        quantity: item.quantity,
        priority: 'MEDIUM',
        status: 'PENDING',
        addedAt: new Date().toISOString(),
        notes: 'Adicionado dos alertas',
        autoAdded: true,
      };
      items.push(newItem);
    });
    
    setStorageData(STORAGE_KEYS.SHOPPING_LIST, items);
    return mockResponse({});
  },
};
