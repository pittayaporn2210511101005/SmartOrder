package com.example.smartOrder.stockHistory;

import com.example.smartOrder.orderdetails.OrderDetails;
import com.example.smartOrder.products.Products;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_history")
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal OldCostPrice;
    private BigDecimal OldSellPrice;
    private BigDecimal NewCostPrice;
    private BigDecimal NewSellPrice;
    private LocalDateTime changedDate;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @ManyToOne
    @JoinColumn(name = "orderdetails_id",nullable = true)
    private OrderDetails orderdetails;


    private int quantityChange; // + เพิ่ม / - ลด
    private String type; // "SALE", "RESTOCK"

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

    public LocalDateTime getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(LocalDateTime changedDate) {
        this.changedDate = changedDate;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public OrderDetails getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(OrderDetails orderdetails) {
        this.orderdetails = orderdetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }
}

