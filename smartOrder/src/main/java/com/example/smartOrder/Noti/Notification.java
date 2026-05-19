package com.example.smartOrder.Noti;

import com.example.smartOrder.dailyReport.DailyReport;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean targetAdmin = true;
    private boolean targetmobile = true;
    private LocalDateTime dateSent;
    private String message;
    private boolean isRead;
    private LocalDateTime readAt;

    @ManyToOne
    @JoinColumn(name = "dailyreport_id")
    private DailyReport dailyReport;

    public Notification() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTargetAdmin() {
        return targetAdmin;
    }

    public void setTargetAdmin(boolean targetAdmin) {
        this.targetAdmin = targetAdmin;
    }

    public boolean isTargetmobile() {
        return targetmobile;
    }

    public void setTargetmobile(boolean targetmobile) {
        this.targetmobile = targetmobile;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public DailyReport getDailyReport() {
        return dailyReport;
    }

    public void setDailyReport(DailyReport dailyReport) {
        this.dailyReport = dailyReport;
    }
}

