package com.viniciusvr.edespensa.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity for ShoppingListItem.
 */
@Entity
@Table(name = "shopping_list_items")
public class ShoppingListItemJpaEntity {

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum Status {
        PENDING, PURCHASED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    private Double quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    private String notes;

    @Column(name = "auto_added")
    private boolean autoAdded;

    public ShoppingListItemJpaEntity() {
    }

    public ShoppingListItemJpaEntity(Long id, ProductJpaEntity product, Double quantity, Priority priority,
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductJpaEntity getProduct() {
        return product;
    }

    public void setProduct(ProductJpaEntity product) {
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
}
