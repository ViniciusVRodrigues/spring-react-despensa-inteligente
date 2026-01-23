package com.viniciusvr.edespensa.domain.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain entity representing an item in the pantry.
 * Contains quantity and expiration tracking for a specific product.
 */
public class PantryItem {

    private Long id;
    private Product product;
    private Double quantity;
    private LocalDate expirationDate;
    private LocalDate addedDate;
    private String location;
    private String notes;

    public PantryItem() {
    }

    public PantryItem(Long id, Product product, Double quantity, LocalDate expirationDate, 
                      LocalDate addedDate, String location, String notes) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.addedDate = addedDate;
        this.location = location;
        this.notes = notes;
    }

    public static PantryItem createNew(Product product, Double quantity, LocalDate expirationDate, 
                                        String location, String notes) {
        return new PantryItem(null, product, quantity, expirationDate, LocalDate.now(), location, notes);
    }

    /**
     * Consumes a specified quantity from this pantry item.
     * @param quantityToConsume the amount to consume
     * @return true if the item is now empty and should be removed
     */
    public boolean consume(Double quantityToConsume) {
        if (quantityToConsume <= 0) {
            throw new IllegalArgumentException("Quantity to consume must be positive");
        }
        if (quantityToConsume > this.quantity) {
            throw new IllegalArgumentException("Cannot consume more than available quantity");
        }
        this.quantity -= quantityToConsume;
        return this.quantity <= 0;
    }

    /**
     * Checks if this item is expired.
     * @return true if the item has an expiration date and it's before today
     */
    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    /**
     * Checks if this item is expiring soon (within the specified days).
     * @param days number of days to check
     * @return true if expiring within the specified days
     */
    public boolean isExpiringSoon(int days) {
        if (expirationDate == null) return false;
        LocalDate threshold = LocalDate.now().plusDays(days);
        return !isExpired() && expirationDate.isBefore(threshold);
    }

    /**
     * Checks if this item has low stock (below the specified threshold).
     * @param threshold the minimum quantity threshold
     * @return true if quantity is below threshold
     */
    public boolean isLowStock(Double threshold) {
        return quantity <= threshold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PantryItem that = (PantryItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PantryItem{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
