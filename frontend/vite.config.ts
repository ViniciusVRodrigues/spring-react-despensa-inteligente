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
        './Dashboard': './src/pages/Dashboard.tsx',
        './Products': './src/pages/Products.tsx',
        './Pantry': './src/pages/Pantry.tsx',
        './ShoppingList': './src/pages/ShoppingList.tsx',
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
