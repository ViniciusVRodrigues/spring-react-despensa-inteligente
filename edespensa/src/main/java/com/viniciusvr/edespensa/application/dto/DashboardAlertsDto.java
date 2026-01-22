package com.viniciusvr.edespensa.application.dto;

import java.util.List;

/**
 * DTO for dashboard alerts about products that need attention.
 */
public record DashboardAlertsDto(
    List<AlertItemDto> expiringSoon,
    List<AlertItemDto> expired,
    List<AlertItemDto> lowStock,
    int totalAlerts
) {
    public static DashboardAlertsDto of(List<AlertItemDto> expiringSoon, 
                                        List<AlertItemDto> expired, 
                                        List<AlertItemDto> lowStock) {
        return new DashboardAlertsDto(
            expiringSoon, 
            expired, 
            lowStock,
            expiringSoon.size() + expired.size() + lowStock.size()
        );
    }
}
