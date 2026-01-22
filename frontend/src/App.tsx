import { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import { MainLayout } from './components/templates/MainLayout';
import { DashboardPage } from './components/pages/DashboardPage';
import { DespensaPage } from './components/pages/DespensaPage';
import { ListaComprasPage } from './components/pages/ListaComprasPage';
import './App.css';

type Tab = 'dashboard' | 'despensa' | 'lista-compras';

/**
 * Componente principal da aplicação Smart Pantry.
 */
function App() {
  const [activeTab, setActiveTab] = useState<Tab>('dashboard');

  const renderPage = () => {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardPage />;
      case 'despensa':
        return <DespensaPage />;
      case 'lista-compras':
        return <ListaComprasPage />;
      default:
        return <DashboardPage />;
    }
  };

  return (
    <>
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
      <MainLayout activeTab={activeTab} onTabChange={setActiveTab}>
        {renderPage()}
      </MainLayout>
    </>
  );
}

export default App;

