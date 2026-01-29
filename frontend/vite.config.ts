import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import federation from '@originjs/vite-plugin-federation'

// https://vite.dev/config/
export default defineConfig({
  base: process.env.VITE_BASE_PATH || '/',
  plugins: [
    react(),
    federation({
      name: 'despensa_inteligente',
      filename: 'remoteEntry.js',
      exposes: {
        './App': './src/App.tsx',
        // Self-sufficient component exports (include all necessary CSS)
        './Dashboard': './src/exports/DashboardExport.tsx',
        './Products': './src/exports/ProductsExport.tsx',
        './Pantry': './src/exports/PantryExport.tsx',
        './ShoppingList': './src/exports/ShoppingListExport.tsx',
        './routes': './src/routes.ts',
      },
      shared: ['react', 'react-dom', 'react-router-dom'],
    }),
  ],
  build: {
    modulePreload: false,
    target: 'esnext',
    minify: false,
    cssCodeSplit: false,
  },
  server: {
    port: Number(process.env.VITE_PORT) || 3002,
    cors: true,
  },
  preview: {
    port: Number(process.env.VITE_PORT) || 3002,
    cors: true,
  },
})
