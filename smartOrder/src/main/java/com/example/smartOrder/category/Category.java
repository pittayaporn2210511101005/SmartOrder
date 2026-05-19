package com.example.smartOrder.category;


import com.example.smartOrder.products.Products;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(length = 10, nullable = false)
    private String id;

    private String Categoryname;

    @OneToMany(mappedBy = "category")
    private List<Products> products = new ArrayList<Products>();

    public Category() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return Categoryname;
    }

    public void setCategoryname(String categoryname) {
        Categoryname = categoryname;
    }
}

