// Self-sufficient export for ShoppingList component
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
import '../components/organisms/ShoppingListItemForm/ShoppingListItemForm.css';
import '../pages/ShoppingList.css';

// Component
import { ShoppingList } from '../pages';

export default ShoppingList;
