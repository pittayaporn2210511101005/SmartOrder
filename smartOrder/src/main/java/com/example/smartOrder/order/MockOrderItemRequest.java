package com.example.smartOrder.order;

public class MockOrderItemRequest {

    private String productId;
    private int quantity;
    private String stockType;

    public MockOrderItemRequest() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }
}