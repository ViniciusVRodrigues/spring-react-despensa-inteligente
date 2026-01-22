package com.viniciusvr.edespensa.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * JPA entity for Product.
 */
@Entity
@Table(name = "products")
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String unit;

    private String description;

    @Column(name = "track_expiration")
    private boolean trackExpiration;

    public ProductJpaEntity() {
    }

    public ProductJpaEntity(Long id, String name, String category, String unit, String description, boolean trackExpiration) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.description = description;
        this.trackExpiration = trackExpiration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTrackExpiration() {
        return trackExpiration;
    }

    public void setTrackExpiration(boolean trackExpiration) {
        this.trackExpiration = trackExpiration;
    }
}
