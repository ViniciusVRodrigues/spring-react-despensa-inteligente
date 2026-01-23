import axios from 'axios';
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

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Product API
export const productApi = {
  getAll: () => api.get<Product[]>('/products'),
  getById: (id: number) => api.get<Product>(`/products/${id}`),
  getByCategory: (category: string) => api.get<Product[]>(`/products/category/${category}`),
  search: (q: string) => api.get<Product[]>(`/products/search?q=${q}`),
  create: (data: ProductRequest) => api.post<Product>('/products', data),
  update: (id: number, data: ProductRequest) => api.put<Product>(`/products/${id}`, data),
  delete: (id: number) => api.delete(`/products/${id}`),
};

// Pantry API
export const pantryApi = {
  getAll: () => api.get<PantryItem[]>('/pantry'),
  getById: (id: number) => api.get<PantryItem>(`/pantry/${id}`),
  getByProduct: (productId: number) => api.get<PantryItem[]>(`/pantry/product/${productId}`),
  search: (q: string) => api.get<PantryItem[]>(`/pantry/search?q=${q}`),
  add: (data: PantryItemRequest) => api.post<PantryItem>('/pantry', data),
  quickPurchase: (data: QuickPurchaseRequest) => api.post<PantryItem>('/pantry/quick-purchase', data),
  update: (id: number, data: PantryItemUpdateRequest) => api.put<PantryItem>(`/pantry/${id}`, data),
  consume: (id: number, data: ConsumeRequest) => api.post<ConsumptionResponse>(`/pantry/${id}/consume`, data),
  discard: (id: number) => api.post(`/pantry/${id}/discard`),
  discardExpired: () => api.post('/pantry/discard-expired'),
  delete: (id: number) => api.delete(`/pantry/${id}`),
};

// Shopping List API
export const shoppingListApi = {
  getAll: () => api.get<ShoppingListItem[]>('/shopping-list'),
  getPending: () => api.get<ShoppingListItem[]>('/shopping-list/pending'),
  getById: (id: number) => api.get<ShoppingListItem>(`/shopping-list/${id}`),
  add: (data: ShoppingListItemRequest) => api.post<ShoppingListItem>('/shopping-list', data),
  update: (id: number, data: Partial<ShoppingListItemRequest>) => 
    api.put<ShoppingListItem>(`/shopping-list/${id}`, data),
  markAsPurchased: (id: number) => api.post(`/shopping-list/${id}/purchased`),
  markAsPurchasedBatch: (ids: number[]) => api.post('/shopping-list/purchased-batch', ids),
  cancel: (id: number) => api.post(`/shopping-list/${id}/cancel`),
  delete: (id: number) => api.delete(`/shopping-list/${id}`),
  clearPurchased: () => api.delete('/shopping-list/clear-purchased'),
};

// Dashboard API
export const dashboardApi = {
  getAlerts: () => api.get<DashboardAlerts>('/dashboard/alerts'),
  addAllAlertsToShoppingList: () => api.post('/dashboard/alerts/add-to-shopping-list'),
  addSelectedAlertsToShoppingList: (pantryItemIds: number[]) =>
    api.post('/dashboard/alerts/add-selected-to-shopping-list', { pantryItemIds }),
};

export default api;
