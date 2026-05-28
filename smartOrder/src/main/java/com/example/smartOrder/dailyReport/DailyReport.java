package com.example.smartOrder.dailyReport;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dailyReport")
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal totalCost = BigDecimal.ZERO;
    private BigDecimal totalSell = BigDecimal.ZERO;

    @Column(unique = true, nullable = false)
    private LocalDate reportDate;

    private BigDecimal profit = BigDecimal.ZERO;
    private String topSelling;
    private int totalOrders;
    private int stockRemaining;

    @OneToMany
    @JoinTable(
            name = "dailyreport_orders",
            joinColumns = @JoinColumn(name = "dailyreport_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    @JsonIgnoreProperties({"dailyReport", "orderDetails"})
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "dailyReport", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"dailyReport"})
    private List<Notification> notifications = new ArrayList<>();

    public DailyReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalSell() {
        return totalSell;
    }

    public void setTotalSell(BigDecimal totalSell) {
        this.totalSell = totalSell;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public String getTopSelling() {
        return topSelling;
    }

    public void setTopSelling(String topSelling) {
        this.topSelling = topSelling;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getStockRemaining() {
        return stockRemaining;
    }

    public void setStockRemaining(int stockRemaining) {
        this.stockRemaining = stockRemaining;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}