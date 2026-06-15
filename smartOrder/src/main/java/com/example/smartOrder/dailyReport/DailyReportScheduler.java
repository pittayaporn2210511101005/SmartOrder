package com.example.smartOrder.dailyReport;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.Noti.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DailyReportScheduler {

    private final DailyReportService dailyReportService;
    private final NotificationRepository notificationRepository;

    public DailyReportScheduler(
            DailyReportService dailyReportService,
            NotificationRepository notificationRepository
    ) {
        this.dailyReportService = dailyReportService;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Bangkok")
    public void generateDailyReportAtNight() {
        LocalDate today = LocalDate.now();

        DailyReport report = dailyReportService.generateReport(today);

        Notification notification = new Notification();

        notification.setTargetAdmin(false);
        notification.setTargetmobile(true);
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setDailyReport(report);

        notification.setMessage(
                "สรุปยอดขายประจำวันที่ " + today +
                        "\nจำนวนออเดอร์: " + report.getTotalOrders() + " รายการ" +
                        "\nยอดขายรวม: " + report.getTotalSell() + " บาท" +
                        "\nต้นทุนรวม: " + report.getTotalCost() + " บาท" +
                        "\nกำไรรวม: " + report.getProfit() + " บาท" +
                        "\nสินค้าขายดี: " + report.getTopSelling()
        );

        notificationRepository.save(notification);

        System.out.println("สร้างรายงานและแจ้งเตือนมือถือแล้ว : " + today);
    }
}