package com.example.smartOrder.stockHistory;

import com.example.smartOrder.orderdetails.OrderDetails;
import com.example.smartOrder.products.Products;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stockHistory")
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal OldCostPrice;
    private BigDecimal OldSellPrice;
    private BigDecimal NewCostPrice;
    private BigDecimal NewSellPrice;
    private LocalDate changedDate;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products products;

    @ManyToOne
    @JoinColumn(name = "orderdetails_id",nullable = true)
    private OrderDetails orderdetails;

    public StockHistory() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getOldCostPrice() {
        return OldCostPrice;
    }

    public void setOldCostPrice(BigDecimal oldCostPrice) {
        OldCostPrice = oldCostPrice;
    }

    public BigDecimal getOldSellPrice() {
        return OldSellPrice;
    }

    public void setOldSellPrice(BigDecimal oldSellPrice) {
        OldSellPrice = oldSellPrice;
    }

    public BigDecimal getNewCostPrice() {
        return NewCostPrice;
    }

    public void setNewCostPrice(BigDecimal newCostPrice) {
        NewCostPrice = newCostPrice;
    }

    public BigDecimal getNewSellPrice() {
        return NewSellPrice;
    }

    public void setNewSellPrice(BigDecimal newSellPrice) {
        NewSellPrice = newSellPrice;
    }

    public LocalDate getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(LocalDate changedDate) {
        this.changedDate = changedDate;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
