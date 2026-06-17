package com.example.smartOrder.dailyReport;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.Noti.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

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

    // ทำงานทุกวันเวลา 21:00 ตามเวลาไทย
    @Scheduled(cron = "0 2 19 * * *", zone = "Asia/Bangkok")
    public void generateDailyReportAtNight() {
        LocalDate today = LocalDate.now();

        boolean alreadyCreatedToday =
                notificationRepository.existsByDailyReport_ReportDateAndTargetmobileTrue(today);

        if (alreadyCreatedToday) {
            System.out.println("วันนี้สร้างแจ้งเตือนสรุปยอดขายไปแล้ว : " + today);
            return;
        }

        DailyReport report = dailyReportService.generateReport(today);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("th", "TH"));

        Notification notification = new Notification();

        notification.setTargetAdmin(false);
        notification.setTargetmobile(true);
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setDailyReport(report);

        notification.setMessage(
                "สรุปยอดขายประจำวันที่ " + today +
                        " ยอดขาย ฿" + nf.format(report.getTotalSell()) +
                        " ต้นทุน ฿" + nf.format(report.getTotalCost()) +
                        " กำไร ฿" + nf.format(report.getProfit())
        );

        notificationRepository.save(notification);

        System.out.println("สร้างรายงานและแจ้งเตือนมือถือแล้ว : " + today);
    }
}