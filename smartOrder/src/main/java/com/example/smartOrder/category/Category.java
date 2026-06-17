package com.example.smartOrder.category;

import com.example.smartOrder.products.Products;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_name", nullable = false)
    private String categoryname;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Products> products = new ArrayList<>();

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
    @PrePersist
    public void prePersist() {
        createAt = LocalDateTime.now();
    }

    @Transient
    private int productCount;

    public Category() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}