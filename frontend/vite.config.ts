import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const port = parseInt(env.VITE_PORT || '5173', 10);

  return {
    plugins: [
      react(),
      federation({
        name: 'smart_pantry',
        filename: 'remoteEntry.js',
        exposes: {
          './App': './src/App.tsx',
          './ProductManager': './src/components/organisms/ProductList',
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
    server: {
      port,
      cors: true,
    },
    preview: {
      port,
    },
  };
});