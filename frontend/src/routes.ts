export interface RouteConfig {
  path: string;
  label: string;
  icon?: string;
  component: string; // Name of the exposed component in module federation
}

export const routes: RouteConfig[] = [
  {
    path: '/',
    label: 'Dashboard',
    icon: '📊',
    component: 'Dashboard',
  },
  {
    path: '/products',
    label: 'Produtos',
    icon: '📦',
    component: 'Products',
  },
  {
    path: '/pantry',
    label: 'Despensa',
    icon: '🏠',
    component: 'Pantry',
  },
  {
    path: '/shopping-list',
    label: 'Lista de Compras',
    icon: '🛒',
    component: 'ShoppingList',
  },
];

export default routes;
