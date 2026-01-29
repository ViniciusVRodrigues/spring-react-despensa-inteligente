// Self-sufficient export for Dashboard component
// Includes all necessary CSS imports for proper styling when used as a remote module

// Global styles
import '../index.css';
import '../App.css';

// Component styles
import '../components/atoms/Badge/Badge.css';
import '../components/atoms/Button/Button.css';
import '../components/atoms/Loading/Loading.css';
import '../components/molecules/Card/Card.css';
import '../components/molecules/AlertCard/AlertCard.css';
import '../pages/Dashboard.css';

// Component
import { Dashboard } from '../pages';

export default Dashboard;
