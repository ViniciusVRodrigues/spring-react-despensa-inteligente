package com.viniciusvr.edespensa.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for pantry thresholds.
 */
@Configuration
@ConfigurationProperties(prefix = "pantry")
public class PantryConfig {

    /**
     * Number of days before expiration to consider an item as "expiring soon".
     */
    private int expiringSoonDays = 7;

    /**
     * Quantity threshold below which an item is considered "low stock".
     */
    private double lowStockThreshold = 2.0;

    public int getExpiringSoonDays() {
        return expiringSoonDays;
    }

    public void setExpiringSoonDays(int expiringSoonDays) {
        this.expiringSoonDays = expiringSoonDays;
    }

    public double getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(double lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
}
