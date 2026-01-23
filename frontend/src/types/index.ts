// Product types
export interface Product {
  id: number;
  name: string;
  category: string;
  unit: string;
  description: string;
  trackExpiration: boolean;
}

export interface ProductRequest {
  name: string;
  category: string;
  unit: string;
  description?: string;
  trackExpiration: boolean;
}

// Pantry types
export interface PantryItem {
  id: number;
  product: Product;
  quantity: number;
  expirationDate: string | null;
  addedDate: string;
  location: string;
  notes: string;
  isExpired: boolean;
  isExpiringSoon: boolean;
  isLowStock: boolean;
}

export interface PantryItemRequest {
  productId: number;
  quantity: number;
  expirationDate?: string;
  location?: string;
  notes?: string;
}

export interface PantryItemUpdateRequest {
  quantity?: number;
  expirationDate?: string;
  location?: string;
  notes?: string;
}

export interface QuickPurchaseRequest {
  productId?: number;
  productName?: string;
  category?: string;
  unit?: string;
  quantity: number;
  expirationDate?: string;
  location?: string;
  notes?: string;
}

// Shopping List types
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
export type Status = 'PENDING' | 'PURCHASED' | 'CANCELLED';

export interface ShoppingListItem {
  id: number;
  product: Product;
  quantity: number;
  priority: Priority;
  status: Status;
  addedAt: string;
  notes: string;
  autoAdded: boolean;
}

export interface ShoppingListItemRequest {
  productId?: number;
  productName?: string;
  quantity?: number;
  priority?: string;
  notes?: string;
}

// Dashboard types
export interface AlertItem {
  pantryItemId: number;
  productName: string;
  quantity: number;
  unit: string;
  expirationDate: string | null;
  daysUntilExpiration: number | null;
  alertType: string;
}

export interface DashboardSummary {
  expiringCount: number;
  expiredCount: number;
  lowStockCount: number;
}

export interface DashboardAlerts {
  expiringSoon: AlertItem[];
  expired: AlertItem[];
  lowStock: AlertItem[];
  totalAlerts: number;
  summary: DashboardSummary;
}

// Consumption types
export interface ConsumeRequest {
  quantity: number;
}

export interface ConsumptionResponse {
  itemsConsumed: number;
  depletedItemIds: number[];
  depletedProductNames: string[];
  message: string;
}
