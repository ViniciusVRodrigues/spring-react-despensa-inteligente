// Self-sufficient export for Products component
// Includes all necessary CSS imports for proper styling when used as a remote module

// Global styles
import '../index.css';
import '../App.css';

// Component styles
import '../components/atoms/Badge/Badge.css';
import '../components/atoms/Button/Button.css';
import '../components/atoms/Input/Input.css';
import '../components/atoms/Loading/Loading.css';
import '../components/molecules/Card/Card.css';
import '../components/molecules/Modal/Modal.css';
import '../components/organisms/DataTable/DataTable.css';
import '../components/organisms/ProductForm/ProductForm.css';
import '../pages/Products.css';

// Component
import { Products } from '../pages';

export default Products;
