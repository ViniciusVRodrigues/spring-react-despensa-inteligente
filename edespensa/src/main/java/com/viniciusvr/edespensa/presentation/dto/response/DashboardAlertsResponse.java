package com.viniciusvr.edespensa.presentation.dto.response;

import com.viniciusvr.edespensa.application.dto.AlertItemDto;
import com.viniciusvr.edespensa.application.dto.DashboardAlertsDto;

import java.util.List;

/**
 * Response DTO for dashboard alerts.
 */
public record DashboardAlertsResponse(
    List<AlertItemDto> expiringSoon,
    List<AlertItemDto> expired,
    List<AlertItemDto> lowStock,
    int totalAlerts,
    DashboardSummary summary
) {
    public record DashboardSummary(
        int expiringCount,
        int expiredCount,
        int lowStockCount
    ) {}
    
    public static DashboardAlertsResponse fromDto(DashboardAlertsDto dto) {
        return new DashboardAlertsResponse(
            dto.expiringSoon(),
            dto.expired(),
            dto.lowStock(),
            dto.totalAlerts(),
            new DashboardSummary(
                dto.expiringSoon().size(),
                dto.expired().size(),
                dto.lowStock().size()
            )
        );
    }
}
