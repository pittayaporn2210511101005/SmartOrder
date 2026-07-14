package com.example.smartOrder.order;

import java.time.LocalDateTime;
import java.util.List;

public class MockOrderRequest {

    private LocalDateTime createdAt;
    private String stockType;
    private List<MockOrderItemRequest> items;

    public MockOrderRequest() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public List<MockOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<MockOrderItemRequest> items) {
        this.items = items;
    }
}