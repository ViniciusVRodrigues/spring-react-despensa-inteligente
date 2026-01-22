import { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import { Header } from './components/organisms/Header';
import { DespensaPage } from './components/pages/DespensaPage';
import { ListaComprasPage } from './components/pages/ListaComprasPage';
import './App.css';

type Tab = 'despensa' | 'lista-compras';

/**
 * Componente principal da aplicação Smart Pantry.
 */
function App() {
  const [activeTab, setActiveTab] = useState<Tab>('despensa');

  return (
    <div className="app">
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: '#363636',
            color: '#fff',
          },
          success: {
            iconTheme: {
              primary: '#16a34a',
              secondary: '#fff',
            },
          },
          error: {
            iconTheme: {
              primary: '#dc2626',
              secondary: '#fff',
            },
          },
        }}
      />
      <Header activeTab={activeTab} onTabChange={setActiveTab} />
      <main className="main-content">
        {activeTab === 'despensa' ? <DespensaPage /> : <ListaComprasPage />}
      </main>
    </div>
  );
}

export default App;

