package com.example.smartOrder.order;

 import com.example.smartOrder.mobile.Mobile;
 import com.example.smartOrder.orderdetails.OrderDetails;
import jakarta.persistence.*;

 import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    private int totalSell;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mobile_id")
    private Mobile mobile;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(columnDefinition = "TEXT")
    private String failReason;

    public Order() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}


