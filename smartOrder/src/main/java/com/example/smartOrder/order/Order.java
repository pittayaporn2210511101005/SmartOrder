package com.example.smartOrder.order;

import com.example.smartOrder.mobile.Mobile;
import com.example.smartOrder.orderdetails.OrderDetails;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Orders")
public class Order {
    @Id
    private String id;
    private LocalDateTime createdAt;
    private int totalSell;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mobile_id")
    private Mobile mobile;

    public Order() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalSell() {
        return totalSell;
    }

    public void setTotalSell(int totalSell) {
        this.totalSell = totalSell;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
