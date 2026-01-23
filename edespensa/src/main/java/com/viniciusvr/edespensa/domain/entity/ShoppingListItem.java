package com.viniciusvr.edespensa.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain entity representing an item on the shopping list.
 */
public class ShoppingListItem {

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum Status {
        PENDING, PURCHASED, CANCELLED
    }

    private Long id;
    private Product product;
    private Double quantity;
    private Priority priority;
    private Status status;
    private LocalDateTime addedAt;
    private String notes;
    private boolean autoAdded;

    public ShoppingListItem() {
    }

    public ShoppingListItem(Long id, Product product, Double quantity, Priority priority, 
                            Status status, LocalDateTime addedAt, String notes, boolean autoAdded) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.priority = priority;
        this.status = status;
        this.addedAt = addedAt;
        this.notes = notes;
        this.autoAdded = autoAdded;
    }

    public static ShoppingListItem createNew(Product product, Double quantity, Priority priority, String notes) {
        return new ShoppingListItem(null, product, quantity, priority, Status.PENDING, 
                                    LocalDateTime.now(), notes, false);
    }

    public static ShoppingListItem createAutoAdded(Product product, Double quantity, String reason) {
        return new ShoppingListItem(null, product, quantity, Priority.MEDIUM, Status.PENDING, 
                                    LocalDateTime.now(), reason, true);
    }

    public void markAsPurchased() {
        this.status = Status.PURCHASED;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAutoAdded() {
        return autoAdded;
    }

    public void setAutoAdded(boolean autoAdded) {
        this.autoAdded = autoAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
