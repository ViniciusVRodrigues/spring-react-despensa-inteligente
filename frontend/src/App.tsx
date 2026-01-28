import { BrowserRouter as Router,Routes, Route } from 'react-router-dom';
import { MainLayout } from './components/templates';
import { Dashboard, Products, Pantry, ShoppingList } from './pages';
import { routes } from './routes';
import './App.css';

// Map component names to actual components
const componentMap: Record<string, React.ComponentType> = {
  Dashboard,
  Products,
  Pantry,
  ShoppingList,
};

function App() {
  return (
    <MainLayout>
      <Router>
        <Routes>
          {routes.map((route) => {
            const Component = componentMap[route.component];
            return Component ? (
              <Route key={route.path} path={route.path} element={<Component />} />
            ) : null;
          })}
        </Routes>
      </Router>
    </MainLayout>
  );
}

export default App;
