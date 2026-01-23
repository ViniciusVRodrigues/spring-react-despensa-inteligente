import React, { useEffect, useState } from 'react';
import { Button, Badge, Loading } from '../components/atoms';
import { Card, AlertCard } from '../components/molecules';
import { dashboardApi } from '../services/api';
import type { DashboardAlerts, AlertItem } from '../types';
import './Dashboard.css';

export const Dashboard: React.FC = () => {
  const [alerts, setAlerts] = useState<DashboardAlerts | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addingToList, setAddingToList] = useState(false);

  const fetchAlerts = async () => {
    try {
      setLoading(true);
      const response = await dashboardApi.getAlerts();
      setAlerts(response.data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar alertas');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAlerts();
  }, []);

  const handleAddAllToShoppingList = async () => {
    try {
      setAddingToList(true);
      await dashboardApi.addAllAlertsToShoppingList();
      alert('Itens adicionados à lista de compras!');
    } catch (err) {
      alert('Erro ao adicionar itens à lista');
      console.error(err);
    } finally {
      setAddingToList(false);
    }
  };

  const renderAlertItem = (item: AlertItem, type: 'expiring' | 'expired' | 'lowStock') => {
    let badgeVariant: 'warning' | 'danger' | 'info' = 'warning';
    let badgeText = '';

    if (type === 'expired') {
      badgeVariant = 'danger';
      badgeText = 'Vencido';
    } else if (type === 'expiring') {
      badgeVariant = 'warning';
      badgeText = item.daysUntilExpiration === 0 
        ? 'Vence hoje!' 
        : `${item.daysUntilExpiration} dias`;
    } else {
      badgeVariant = 'info';
      badgeText = 'Estoque baixo';
    }

    return (
      <AlertCard
        key={item.pantryItemId}
        title={item.productName}
        subtitle={`${item.quantity} ${item.unit}`}
        badge={{ text: badgeText, variant: badgeVariant }}
      />
    );
  };

  if (loading) {
    return <Loading size="large" text="Carregando alertas..." />;
  }

  if (error) {
    return (
      <div className="dashboard-error">
        <p>{error}</p>
        <Button onClick={fetchAlerts}>Tentar novamente</Button>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1 className="page-title">Dashboard</h1>
        {alerts && alerts.totalAlerts > 0 && (
          <Button onClick={handleAddAllToShoppingList} disabled={addingToList}>
            {addingToList ? 'Adicionando...' : 'Adicionar Tudo à Lista'}
          </Button>
        )}
      </div>

      <div className="dashboard-summary">
        <div className="summary-card summary-expiring">
          <span className="summary-number">{alerts?.summary.expiringCount || 0}</span>
          <span className="summary-label">Vencendo em breve</span>
        </div>
        <div className="summary-card summary-expired">
          <span className="summary-number">{alerts?.summary.expiredCount || 0}</span>
          <span className="summary-label">Vencidos</span>
        </div>
        <div className="summary-card summary-low-stock">
          <span className="summary-number">{alerts?.summary.lowStockCount || 0}</span>
          <span className="summary-label">Estoque baixo</span>
        </div>
      </div>

      {alerts?.totalAlerts === 0 ? (
        <Card className="no-alerts">
          <div className="no-alerts-content">
            <span className="no-alerts-icon">✓</span>
            <h3>Tudo em ordem!</h3>
            <p>Não há alertas no momento.</p>
          </div>
        </Card>
      ) : (
        <div className="dashboard-grid">
          {alerts?.expiringSoon && alerts.expiringSoon.length > 0 && (
            <Card 
              title="Vencendo em Breve" 
              actions={<Badge variant="warning">{alerts.expiringSoon.length}</Badge>}
            >
              <div className="alert-list">
                {alerts.expiringSoon.map((item) => renderAlertItem(item, 'expiring'))}
              </div>
            </Card>
          )}

          {alerts?.expired && alerts.expired.length > 0 && (
            <Card 
              title="Vencidos" 
              actions={<Badge variant="danger">{alerts.expired.length}</Badge>}
            >
              <div className="alert-list">
                {alerts.expired.map((item) => renderAlertItem(item, 'expired'))}
              </div>
            </Card>
          )}

          {alerts?.lowStock && alerts.lowStock.length > 0 && (
            <Card 
              title="Estoque Baixo" 
              actions={<Badge variant="info">{alerts.lowStock.length}</Badge>}
            >
              <div className="alert-list">
                {alerts.lowStock.map((item) => renderAlertItem(item, 'lowStock'))}
              </div>
            </Card>
          )}
        </div>
      )}
    </div>
  );
};
