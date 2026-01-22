package com.viniciusvr.edespensa.domain.entity;

import java.util.Objects;

/**
 * Domain entity representing a product in the pantry system.
 * This is a pure domain object with no framework dependencies.
 */
public class Product {

    private Long id;
    private String name;
    private String category;
    private String unit;
    private String description;
    private boolean trackExpiration;

    public Product() {
    }

    public Product(Long id, String name, String category, String unit, String description, boolean trackExpiration) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.description = description;
        this.trackExpiration = trackExpiration;
    }

    public static Product createNew(String name, String category, String unit, String description, boolean trackExpiration) {
        return new Product(null, name, category, unit, description, trackExpiration);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
