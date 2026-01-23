import { Routes, Route } from 'react-router-dom';
import { MainLayout } from './components/templates';
import { Dashboard, Products, Pantry, ShoppingList } from './pages';
import './App.css';

function App() {
  return (
      <MainLayout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/products" element={<Products />} />
          <Route path="/pantry" element={<Pantry />} />
          <Route path="/shopping-list" element={<ShoppingList />} />
        </Routes>
      </MainLayout>
  );
}

export default App;
