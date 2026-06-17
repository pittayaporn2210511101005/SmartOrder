package com.example.smartOrder.Noti;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByTargetmobileTrueOrderByDateSentDesc();

    List<Notification> findByTargetmobileTrueAndIsReadFalseOrderByDateSentDesc();

    boolean existsByDailyReport_ReportDateAndTargetmobileTrue(LocalDate reportDate);
}