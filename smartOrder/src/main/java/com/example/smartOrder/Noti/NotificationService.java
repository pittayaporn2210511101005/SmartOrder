package com.example.smartOrder.Noti;

import com.example.smartOrder.dailyReport.DailyReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getMobileNotifications() {
        return notificationRepository.findByTargetmobileTrueOrderByDateSentDesc();
    }

    public List<Notification> getUnreadMobileNotifications() {
        return notificationRepository.findByTargetmobileTrueAndIsReadFalseOrderByDateSentDesc();
    }

    public Notification markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบแจ้งเตือน id: " + id));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public Notification createDailyReportNotification(DailyReport report) {
        Notification notification = new Notification();

        notification.setTargetAdmin(true);
        notification.setTargetmobile(true);
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        notification.setDailyReport(report);

        notification.setMessage(
                "สรุปยอดขายประจำวันที่ " + report.getReportDate() +
                        " ยอดขายรวม ฿" + report.getTotalSell() +
                        " กำไร ฿" + report.getProfit() +
                        " สินค้าขายดี " + report.getTopSelling()
        );

        return notificationRepository.save(notification);
    }
}