package com.example.smartOrder.category;

import com.example.smartOrder.products.Products;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(length = 10, nullable = false)
    private String id;

    @Column(name = "Categoryname", nullable = false)
    private String categoryname;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Products> products = new ArrayList<>();

    @Transient
    private int productCount;

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}