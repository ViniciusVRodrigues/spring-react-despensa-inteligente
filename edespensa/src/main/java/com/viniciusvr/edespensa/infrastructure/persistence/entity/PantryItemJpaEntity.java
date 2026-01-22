package com.viniciusvr.edespensa.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * JPA entity for PantryItem.
 */
@Entity
@Table(name = "pantry_items")
public class PantryItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "added_date", nullable = false)
    private LocalDate addedDate;

    private String location;

    private String notes;

    public PantryItemJpaEntity() {
    }

    public PantryItemJpaEntity(Long id, ProductJpaEntity product, Double quantity, LocalDate expirationDate, 
                                LocalDate addedDate, String location, String notes) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.addedDate = addedDate;
        this.location = location;
        this.notes = notes;
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
}
